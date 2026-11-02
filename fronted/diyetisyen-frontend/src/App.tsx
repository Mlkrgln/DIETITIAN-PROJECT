import { type Dispatch, type FormEvent, type SetStateAction, useEffect, useState } from 'react'
import './App.css'

type JwtClaims = { sub?: string; roles?: string[] | string; userId?: number; exp?: number }
type CustomerProfile = { id: number; authUserId: number; phone?: string; birthDate?: string; gender?: string }
type Appointment = { id: number; customerUserId: number; appointmentDate: string; status: string; cancellationReason?: string }
type DietMeal = { id: number; mealDate: string; mealType: string; description: string; calories?: number }
type DietPlan = { id: number; customerUserId: number; title: string; startDate: string; endDate: string; meals: DietMeal[] }
type ApiOptions = { method?: string; body?: unknown }

class ApiError extends Error {
  readonly status: number
  constructor(message: string, status: number) { super(message); this.status = status }
}

const endpoints = {
  auth: import.meta.env.VITE_AUTH_API_URL || 'http://localhost:8080',
  customers: import.meta.env.VITE_CUSTOMER_API_URL || 'http://localhost:8081',
  appointments: import.meta.env.VITE_APPOINTMENT_API_URL || 'http://localhost:8082',
  dietPlans: import.meta.env.VITE_DIET_PLAN_API_URL || 'http://localhost:8083',
}

function getClaims(token: string): JwtClaims | null {
  try {
    const part = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')
    return JSON.parse(window.atob(part.padEnd(Math.ceil(part.length / 4) * 4, '='))) as JwtClaims
  } catch {
    return null
  }
}

async function request<T>(base: string, path: string, token: string, options: ApiOptions = {}): Promise<T> {
  const response = await fetch(`${base}${path}`, {
    method: options.method || 'GET',
    headers: {
      Authorization: `Bearer ${token}`,
      ...(options.body === undefined ? {} : { 'Content-Type': 'application/json' }),
    },
    ...(options.body === undefined ? {} : { body: JSON.stringify(options.body) }),
  })
  const text = await response.text()
  const payload = text ? JSON.parse(text) as unknown : undefined
  if (!response.ok) {
    const detail = typeof payload === 'object' && payload !== null && 'detail' in payload
      ? String(payload.detail)
      : typeof payload === 'object' && payload !== null && 'message' in payload
        ? String(payload.message)
        : `İstek başarısız (${response.status})`
    throw new ApiError(detail, response.status)
  }
  return payload as T
}

function roleNames(claims: JwtClaims | null): string[] {
  if (!claims?.roles) return []
  return Array.isArray(claims.roles) ? claims.roles : claims.roles.split(',')
}

type StateSetter<T> = Dispatch<SetStateAction<T>>
type DashboardSetters = {
  setLoading: StateSetter<boolean>
  setError: StateSetter<string>
  setProfile: StateSetter<CustomerProfile | null>
  setAppointments: StateSetter<Appointment[]>
  setPlans: StateSetter<DietPlan[]>
  setCustomers: StateSetter<CustomerProfile[]>
}

async function loadDashboard(token: string, isStaff: boolean, setters: DashboardSetters) {
  setters.setLoading(true)
  setters.setError('')
  const tasks: Promise<void>[] = []
  if (isStaff) {
    tasks.push(request<CustomerProfile[]>(endpoints.customers, '/api/customers', token).then(setters.setCustomers))
    tasks.push(request<Appointment[]>(endpoints.appointments, '/api/appointments', token).then(setters.setAppointments))
  } else {
    tasks.push(request<CustomerProfile>(endpoints.customers, '/api/customers/me', token)
      .then(setters.setProfile)
      .catch(cause => { if (cause instanceof ApiError && cause.status === 404) setters.setProfile(null); else throw cause }))
    tasks.push(request<Appointment[]>(endpoints.appointments, '/api/appointments/me', token).then(setters.setAppointments))
    tasks.push(request<DietPlan[]>(endpoints.dietPlans, '/api/diet-plans/me', token).then(setters.setPlans))
  }
  const results = await Promise.allSettled(tasks)
  const failure = results.find(result => result.status === 'rejected')
  if (failure?.status === 'rejected') setters.setError(failure.reason instanceof Error ? failure.reason.message : 'Veriler alınamadı')
  setters.setLoading(false)
}

function AuthScreen({ onLogin }: { onLogin: (token: string) => void }) {
  const [registering, setRegistering] = useState(false)
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError('')
    setBusy(true)
    try {
      const path = registering ? '/api/auth/register/customer' : '/api/auth/login'
      const body = registering ? { username, email, password } : { username, password }
      const response = await fetch(`${endpoints.auth}${path}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      })
      const payload = await response.json() as { token?: string; detail?: string; message?: string }
      if (!response.ok || !payload.token) throw new Error(payload.detail || payload.message || 'Giriş başarısız oldu')
      onLogin(payload.token)
    } catch (cause) {
      setError(cause instanceof Error ? cause.message : 'İstek tamamlanamadı')
    } finally {
      setBusy(false)
    }
  }

  return (
    <main className="auth-shell">
      <section className="auth-card">
        <div className="brand-mark">D</div>
        <p className="eyebrow">DİYETİSYEN • DANIŞAN</p>
        <h1>{registering ? 'Hesabını oluştur' : 'Tekrar hoş geldin'}</h1>
        <p className="muted">Randevularını ve beslenme planlarını tek yerde takip et.</p>
        <form onSubmit={submit} className="form-stack">
          <label>Kullanıcı adı<input required autoComplete="username" value={username} onChange={e => setUsername(e.target.value)} /></label>
          {registering && <label>E-posta<input required type="email" autoComplete="email" value={email} onChange={e => setEmail(e.target.value)} /></label>}
          <label>Şifre<input required type="password" autoComplete={registering ? 'new-password' : 'current-password'} value={password} onChange={e => setPassword(e.target.value)} /></label>
          {error && <p className="error-message">{error}</p>}
          <button className="button primary full" disabled={busy}>{busy ? 'Bekleyin…' : registering ? 'Danışan hesabı oluştur' : 'Giriş yap'}</button>
        </form>
        <button className="text-button" onClick={() => { setRegistering(!registering); setError('') }}>
          {registering ? 'Zaten hesabın var mı? Giriş yap' : 'Yeni danışan mısın? Hesap oluştur'}
        </button>
      </section>
      <aside className="auth-aside">
        <span className="aside-kicker">İyi yaşam, sürdürülebilir alışkanlıklarla başlar.</span>
        <div className="plant-art" aria-hidden="true"><span /><span /><span /><i /></div>
        <p>Planını takip et. Küçük adımlarla ilerle.</p>
      </aside>
    </main>
  )
}

function App() {
  const [token, setToken] = useState(() => localStorage.getItem('dietitian-token') || '')
  const claims = token ? getClaims(token) : null
  const roles = roleNames(claims)
  const isStaff = roles.includes('ROLE_ADMIN') || roles.includes('ROLE_DIETITIAN')
  const [tab, setTab] = useState<'overview' | 'appointments' | 'plans' | 'profile'>('overview')
  const [profile, setProfile] = useState<CustomerProfile | null>(null)
  const [appointments, setAppointments] = useState<Appointment[]>([])
  const [plans, setPlans] = useState<DietPlan[]>([])
  const [customers, setCustomers] = useState<CustomerProfile[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [notice, setNotice] = useState('')

  function logout() {
    localStorage.removeItem('dietitian-token')
    setToken('')
    setProfile(null)
    setAppointments([])
    setPlans([])
    setCustomers([])
  }

  function refresh() {
    return loadDashboard(token, isStaff, { setLoading, setError, setProfile, setAppointments, setPlans, setCustomers })
  }

  useEffect(() => {
    if (!token) return
    const exp = claims?.exp
    const timer = window.setTimeout(() => {
      if (exp && exp * 1000 < Date.now()) {
        logout()
        return
      }
      void loadDashboard(token, isStaff, { setLoading, setError, setProfile, setAppointments, setPlans, setCustomers })
    }, 0)
    return () => window.clearTimeout(timer)
  }, [claims?.exp, isStaff, token])

  function login(newToken: string) {
    localStorage.setItem('dietitian-token', newToken)
    setToken(newToken)
    setNotice('Giriş başarılı.')
  }

  async function perform(action: () => Promise<unknown>, success: string) {
    setError('')
    setNotice('')
    try {
      await action()
      setNotice(success)
      await refresh()
    } catch (cause) {
      setError(cause instanceof Error ? cause.message : 'İşlem tamamlanamadı')
    }
  }

  if (!token || !claims) return <AuthScreen onLogin={login} />

  const displayRole = roles.includes('ROLE_ADMIN') ? 'Yönetici' : roles.includes('ROLE_DIETITIAN') ? 'Diyetisyen' : 'Danışan'
  const displayName = claims.sub || 'Kullanıcı'

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <a className="brand" href="#top"><span className="brand-mark small">D</span><span>denge<span className="brand-dot">.</span></span></a>
        <div className="side-label">ÇALIŞMA ALANI</div>
        <nav className="side-nav" aria-label="Ana menü">
          <NavButton active={tab === 'overview'} onClick={() => setTab('overview')} icon="⌂">Genel bakış</NavButton>
          <NavButton active={tab === 'appointments'} onClick={() => setTab('appointments')} icon="◷">Randevular</NavButton>
          {!isStaff && <NavButton active={tab === 'plans'} onClick={() => setTab('plans')} icon="▤">Beslenme planım</NavButton>}
          {!isStaff && <NavButton active={tab === 'profile'} onClick={() => setTab('profile')} icon="◉">Profilim</NavButton>}
        </nav>
        <div className="sidebar-bottom">
          <div className="help-card"><span>Birlikte daha iyi</span><p>Sağlıklı alışkanlıklar, zamanla kurulur.</p></div>
          <button className="user-chip" onClick={logout}><span className="avatar">{displayName.slice(0, 1).toUpperCase()}</span><span className="user-meta"><b>{displayName}</b><small>{displayRole} · Çıkış yap</small></span><span className="more">↗</span></button>
        </div>
      </aside>

      <main className="main-content" id="top">
        <header className="topbar"><div className="breadcrumb">Denge <span>/</span> {tabTitle(tab)}</div><div className="topbar-right"><span className="today-label">Sağlıklı günler ✦</span><span className="avatar top-avatar">{displayName.slice(0, 1).toUpperCase()}</span></div></header>
        <div className="content-wrap">
          <div className="page-heading"><div><p className="eyebrow">{isStaff ? 'DANIŞAN YÖNETİMİ' : 'KİŞİSEL ALANIN'}</p><h1>{greeting()}, {displayName}</h1><p className="muted">{isStaff ? 'Danışanlarını, randevuları ve beslenme planlarını buradan yönet.' : 'İlerlemeni takip et, iyi hissettiren rutinini sürdür.'}</p></div><button className="button quiet" onClick={() => void refresh()} disabled={loading}>{loading ? 'Yenileniyor…' : '↻ Yenile'}</button></div>
          {error && <div className="notice error-banner">{error}</div>}
          {notice && <div className="notice success-banner">{notice}</div>}

          {tab === 'overview' && <Overview isStaff={isStaff} appointments={appointments} plans={plans} profile={profile} customers={customers} setTab={setTab} />}
          {tab === 'profile' && !isStaff && <ProfilePanel token={token} profile={profile} onSaved={() => void perform(() => refresh(), 'Profil kaydedildi.')} />}
          {tab === 'appointments' && !isStaff && <AppointmentsPanel token={token} isStaff={false} appointments={appointments} onChange={refresh} onError={setError} onNotice={setNotice} />}
          {tab === 'plans' && !isStaff && <PlansPanel plans={plans} />}
          {isStaff && <StaffPanel tab={tab} token={token} customers={customers} appointments={appointments} onChange={refresh} onError={setError} onNotice={setNotice} />}
        </div>
      </main>
    </div>
  )
}

function NavButton({ active, onClick, icon, children }: { active: boolean; onClick: () => void; icon: string; children: string }) {
  return <button className={`nav-button ${active ? 'active' : ''}`} onClick={onClick}><span className="nav-icon">{icon}</span>{children}{active && <i />}</button>
}

function Overview({ isStaff, appointments, plans, profile, customers, setTab }: { isStaff: boolean; appointments: Appointment[]; plans: DietPlan[]; profile: CustomerProfile | null; customers: CustomerProfile[]; setTab: (tab: 'overview' | 'appointments' | 'plans' | 'profile') => void }) {
  const nextAppointment = appointments.filter(item => item.status === 'CREATED').sort((a, b) => a.appointmentDate.localeCompare(b.appointmentDate))[0]
  return <>
    {!isStaff && !profile && <div className="notice profile-prompt"><div><b>Profilini tamamla</b><p>Randevu ve beslenme takibine başlamak için kısa profil bilgilerini ekle.</p></div><button className="button primary" onClick={() => setTab('profile')}>Profili oluştur <span>→</span></button></div>}
    <section className="stat-grid">
      <StatCard label={isStaff ? 'Aktif randevu' : 'Yaklaşan randevu'} value={isStaff ? String(appointments.filter(item => item.status === 'CREATED').length) : nextAppointment ? formatDate(nextAppointment.appointmentDate) : 'Yok'} foot={isStaff ? 'Planlanmış görüşmeler' : nextAppointment ? 'Takvimindeki sıradaki görüşme' : 'Yeni bir görüşme planlayabilirsin'} icon="◷" tone="peach" />
      <StatCard label={isStaff ? 'Danışan sayısı' : 'Beslenme planı'} value={isStaff ? String(customers.length) : String(plans.length)} foot={isStaff ? 'Kayıtlı danışanlar' : plans.length ? 'Diyetisyenin tarafından hazırlandı' : 'Henüz plan eklenmedi'} icon="▤" tone="sage" />
      <StatCard label="Tamamlanan görüşme" value={String(appointments.filter(item => item.status === 'COMPLETED').length)} foot="Görüşme geçmişin" icon="✳" tone="lavender" />
    </section>
    <div className="section-heading"><div><p className="eyebrow">BUGÜN VE SONRASI</p><h2>{isStaff ? 'Randevu akışı' : 'Yaklaşan randevuların'}</h2></div><button className="text-button arrow-link" onClick={() => setTab('appointments')}>Tüm randevular <span>→</span></button></div>
    {appointments.length ? <AppointmentList appointments={appointments.slice(0, 4)} isStaff={isStaff} onAction={() => undefined} /> : <EmptyState title="Henüz randevu yok" description={isStaff ? 'Danışanlar randevu aldıkça burada görünecek.' : 'Yeni bir randevu oluşturarak başlayabilirsin.'} />}
    {!isStaff && <div className="section-heading plan-heading"><div><p className="eyebrow">SANA ÖZEL</p><h2>Beslenme planların</h2></div><button className="text-button arrow-link" onClick={() => setTab('plans')}>Planlara git <span>→</span></button></div>}
    {!isStaff && plans.slice(0, 2).map(plan => <PlanCard key={plan.id} plan={plan} />)}
  </>
}

function StatCard({ label, value, foot, icon, tone }: { label: string; value: string; foot: string; icon: string; tone: string }) {
  return <article className="stat-card"><div className={`stat-icon ${tone}`}>{icon}</div><p>{label}</p><strong>{value}</strong><small>{foot}</small></article>
}

function ProfilePanel({ token, profile, onSaved }: { token: string; profile: CustomerProfile | null; onSaved: () => void }) {
  const [phone, setPhone] = useState(profile?.phone || '')
  const [birthDate, setBirthDate] = useState(profile?.birthDate || '')
  const [gender, setGender] = useState(profile?.gender || '')
  const [saving, setSaving] = useState(false)
  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setSaving(true)
    try {
      await request(endpoints.customers, '/api/customers/me', token, { method: 'POST', body: { phone, birthDate: birthDate || null, gender } })
      onSaved()
    } catch (cause) {
      window.alert(cause instanceof Error ? cause.message : 'Profil kaydedilemedi')
    } finally {
      setSaving(false)
    }
  }
  return <section className="panel"><div className="panel-heading"><div><p className="eyebrow">DANIŞAN PROFİLİ</p><h2>{profile ? 'Profil bilgilerin' : 'Profilini oluştur'}</h2></div><span className="panel-icon">◉</span></div><p className="muted">Bilgilerin diyetisyeninin sana daha uygun bir plan hazırlamasına yardımcı olur.</p><form className="form-grid" onSubmit={submit}><label>Telefon<input type="tel" maxLength={30} value={phone} onChange={e => setPhone(e.target.value)} placeholder="05xx xxx xx xx" /></label><label>Doğum tarihi<input type="date" value={birthDate} onChange={e => setBirthDate(e.target.value)} /></label><label>Cinsiyet<input maxLength={50} value={gender} onChange={e => setGender(e.target.value)} placeholder="Belirtmek istemiyorsan boş bırak" /></label><div className="form-actions"><button className="button primary" disabled={saving || !!profile}>{profile ? 'Profil oluşturuldu' : saving ? 'Kaydediliyor…' : 'Profili kaydet'}</button></div></form></section>
}

function AppointmentsPanel({ token, isStaff, appointments, onChange, onError, onNotice }: { token: string; isStaff: boolean; appointments: Appointment[]; onChange: () => Promise<void>; onError: (message: string) => void; onNotice: (message: string) => void }) {
  const [date, setDate] = useState('')
  const [busyId, setBusyId] = useState<number | null>(null)
  async function create(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    onError('')
    try {
      await request(endpoints.appointments, '/api/appointments', token, { method: 'POST', body: { appointmentDate: date } })
      setDate('')
      onNotice('Randevu oluşturuldu.')
      await onChange()
    } catch (cause) { onError(cause instanceof Error ? cause.message : 'Randevu oluşturulamadı') }
  }
  async function update(id: number, action: 'cancel' | 'complete') {
    setBusyId(id)
    onError('')
    try {
      const path = action === 'cancel' ? `/api/appointments/${id}/cancel` : `/api/appointments/${id}/complete`
      await request(endpoints.appointments, path, token, { method: 'POST', body: action === 'cancel' ? { reason: 'Danışan tarafından iptal edildi' } : undefined })
      onNotice(action === 'cancel' ? 'Randevu iptal edildi.' : 'Randevu tamamlandı.')
      await onChange()
    } catch (cause) { onError(cause instanceof Error ? cause.message : 'İşlem tamamlanamadı') }
    finally { setBusyId(null) }
  }
  return <>
    {!isStaff && <section className="panel create-appointment"><div><p className="eyebrow">YENİ GÖRÜŞME</p><h2>Randevu oluştur</h2><p className="muted">Sana uygun bir gün ve saat seç.</p></div><form onSubmit={create} className="inline-form"><input required type="datetime-local" value={date} onChange={e => setDate(e.target.value)} /><button className="button primary">Randevu al <span>→</span></button></form></section>}
    <section className="panel"><div className="panel-heading"><div><p className="eyebrow">TAKVİM</p><h2>{isStaff ? 'Tüm randevular' : 'Randevu geçmişin'}</h2></div><span className="count-pill">{appointments.length} kayıt</span></div><AppointmentList appointments={appointments} isStaff={isStaff} onAction={update} busyId={busyId} /></section>
  </>
}

function AppointmentList({ appointments, isStaff, onAction, busyId }: { appointments: Appointment[]; isStaff: boolean; onAction: (id: number, action: 'cancel' | 'complete') => void; busyId?: number | null }) {
  if (!appointments.length) return <EmptyState title="Gösterilecek randevu yok" description="Yeni randevuların burada listelenecek." />
  return <div className="appointment-list">{appointments.map(item => <article className="appointment-row" key={item.id}><div className="date-tile"><b>{new Date(item.appointmentDate).toLocaleDateString('tr-TR', { day: '2-digit' })}</b><span>{new Date(item.appointmentDate).toLocaleDateString('tr-TR', { month: 'short' })}</span></div><div className="appointment-info"><b>{isStaff ? `Danışan #${item.customerUserId}` : 'Beslenme danışmanlığı'}</b><span>{new Date(item.appointmentDate).toLocaleString('tr-TR', { weekday: 'long', hour: '2-digit', minute: '2-digit' })}</span></div><StatusBadge status={item.status} />{item.status === 'CREATED' && <div className="row-actions">{isStaff ? <button className="button small-button" disabled={busyId === item.id} onClick={() => onAction(item.id, 'complete')}>Tamamla</button> : <button className="icon-button" aria-label="Randevuyu iptal et" disabled={busyId === item.id} onClick={() => onAction(item.id, 'cancel')}>×</button>}</div>}</article>)}</div>
}

function PlansPanel({ plans }: { plans: DietPlan[] }) {
  return <section className="panel"><div className="panel-heading"><div><p className="eyebrow">DİYETİSYENİNİN HAZIRLADIĞI</p><h2>Beslenme planların</h2></div><span className="count-pill">{plans.length} plan</span></div>{plans.length ? plans.map(plan => <PlanCard key={plan.id} plan={plan} />) : <EmptyState title="Henüz planın yok" description="Diyetisyenin planını eklediğinde burada görüntüleyebilirsin." />}</section>
}

function PlanCard({ plan }: { plan: DietPlan }) {
  return <article className="plan-card"><div className="plan-top"><div className="plan-symbol">✳</div><div className="plan-title"><b>{plan.title}</b><span>{formatDay(plan.startDate)} – {formatDay(plan.endDate)}</span></div><span className="plan-count">{plan.meals.length} öğün</span></div><div className="meal-list">{plan.meals.map(meal => <div className="meal-row" key={meal.id}><time>{formatDay(meal.mealDate)}</time><b>{meal.mealType}</b><span>{meal.description}</span>{meal.calories != null && <small>{meal.calories} kcal</small>}</div>)}</div></article>
}

function StaffPanel({ tab, token, customers, appointments, onChange, onError, onNotice }: { tab: string; token: string; customers: CustomerProfile[]; appointments: Appointment[]; onChange: () => Promise<void>; onError: (message: string) => void; onNotice: (message: string) => void }) {
  const [customerUserId, setCustomerUserId] = useState('')
  const [title, setTitle] = useState('Haftalık beslenme planı')
  const [startDate, setStartDate] = useState('')
  const [endDate, setEndDate] = useState('')
  const [mealType, setMealType] = useState('Kahvaltı')
  const [description, setDescription] = useState('')
  const [calories, setCalories] = useState('')
  if (tab === 'appointments') return <section className="panel"><div className="panel-heading"><div><p className="eyebrow">TAKVİM</p><h2>Randevu yönetimi</h2></div><span className="count-pill">{appointments.length} kayıt</span></div><AppointmentList appointments={appointments} isStaff onAction={async (id, action) => { onError(''); try { const path = action === 'cancel' ? `/api/appointments/admin/${id}/cancel` : `/api/appointments/${id}/${action}`; await request(endpoints.appointments, path, token, { method: 'POST', body: action === 'cancel' ? { reason: 'Diyetisyen tarafından iptal edildi' } : undefined }); onNotice(action === 'complete' ? 'Randevu tamamlandı.' : 'Randevu iptal edildi.'); await onChange() } catch (cause) { onError(cause instanceof Error ? cause.message : 'İşlem tamamlanamadı') } }} /></section>
  return <div className="staff-grid"><section className="panel"><div className="panel-heading"><div><p className="eyebrow">DANIŞANLAR</p><h2>Danışan listen</h2></div><span className="count-pill">{customers.length} kişi</span></div>{customers.length ? <div className="customer-list">{customers.map(customer => <div className="customer-row" key={customer.id}><span className="avatar">{String(customer.authUserId).slice(-1)}</span><div><b>Danışan #{customer.authUserId}</b><small>{customer.phone || 'Telefon eklenmemiş'}{customer.birthDate ? ` · ${formatDay(customer.birthDate)}` : ''}</small></div><span className="customer-id">ID {customer.id}</span></div>)}</div> : <EmptyState title="Danışan bulunamadı" description="Kayıt olan danışanlar burada görünür." />}</section>
    <section className="panel"><div className="panel-heading"><div><p className="eyebrow">KİŞİYE ÖZEL</p><h2>Beslenme planı oluştur</h2></div><span className="panel-icon">✳</span></div><form className="form-stack staff-form" onSubmit={async event => { event.preventDefault(); onError(''); try { await request(endpoints.dietPlans, '/api/diet-plans', token, { method: 'POST', body: { customerUserId: Number(customerUserId), title, startDate, endDate, meals: [{ mealDate: startDate, mealType, description, calories: calories ? Number(calories) : null }] } }); onNotice('Beslenme planı kaydedildi.'); setDescription(''); await onChange() } catch (cause) { onError(cause instanceof Error ? cause.message : 'Plan oluşturulamadı') } }}><label>Danışan<select required value={customerUserId} onChange={e => setCustomerUserId(e.target.value)}><option value="">Danışan seç</option>{customers.map(customer => <option key={customer.id} value={customer.authUserId}>Danışan #{customer.authUserId}</option>)}</select></label><label>Plan adı<input required maxLength={120} value={title} onChange={e => setTitle(e.target.value)} /></label><div className="two-fields"><label>Başlangıç<input required type="date" value={startDate} onChange={e => setStartDate(e.target.value)} /></label><label>Bitiş<input required type="date" value={endDate} onChange={e => setEndDate(e.target.value)} /></label></div><label>Öğün adı<input required maxLength={40} value={mealType} onChange={e => setMealType(e.target.value)} /></label><label>İçerik / öneri<textarea required maxLength={1000} rows={3} value={description} onChange={e => setDescription(e.target.value)} placeholder="Öğün içeriğini yaz" /></label><label>Kalori (isteğe bağlı)<input type="number" min="0" value={calories} onChange={e => setCalories(e.target.value)} /></label><button className="button primary">Planı kaydet <span>→</span></button></form></section></div>
}

function StatusBadge({ status }: { status: string }) {
  const label = status === 'CREATED' ? 'Planlandı' : status === 'COMPLETED' ? 'Tamamlandı' : 'İptal edildi'
  return <span className={`status-badge ${status.toLowerCase()}`}><i />{label}</span>
}

function EmptyState({ title, description }: { title: string; description: string }) {
  return <div className="empty-state"><span>✳</span><b>{title}</b><p>{description}</p></div>
}

function tabTitle(tab: string) {
  return ({ overview: 'Genel bakış', appointments: 'Randevular', plans: 'Beslenme planım', profile: 'Profilim' } as Record<string, string>)[tab] || tab
}
function greeting() { const hour = new Date().getHours(); return hour < 12 ? 'Günaydın' : hour < 18 ? 'Merhaba' : 'İyi akşamlar' }
function formatDate(value: string) { return new Date(value).toLocaleDateString('tr-TR', { day: '2-digit', month: 'short' }) }
function formatDay(value: string) { return new Date(`${value.slice(0, 10)}T12:00:00`).toLocaleDateString('tr-TR', { day: 'numeric', month: 'long', year: 'numeric' }) }

export default App
