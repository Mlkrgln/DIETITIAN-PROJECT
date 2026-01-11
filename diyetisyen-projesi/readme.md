# 🥗 Diyetisyen Yönetim Sistemi (Backend)

Bu proje, **diyetisyen–müşteri–randevu** süreçlerini yöneten, **JWT tabanlı güvenlik** ile korunan, **modüler ve test edilebilir** bir Spring Boot backend uygulamasıdır.

Amaç, gerçek projelerde kullanılan **Auth + User merkezli** bir mimariyi, temiz katman ayrımı ve production mindset ile ortaya koymaktır.

---

## 🎯 Projenin Amacı

- Diyetisyenlerin müşterilerini ve randevularını yönetebilmesi
- Kullanıcıların (ADMIN, DIETITIAN, CUSTOMER) rol bazlı yetkilendirilmesi
- Güvenli giriş (JWT)
- Test edilebilir ve genişletilebilir mimari

---

## 🧱 Mimari Yaklaşım

Proje **modüler monolith** yapıda tasarlanmıştır.
Her modül kendi sorumluluğuna sahiptir ve aşağıdaki katmanlardan oluşur:

```
controller  →  service  →  repository
              ↓
           dto / mapper
              ↓
            entity
```

### Kullanılan Modüller

- **auth** → Kimlik doğrulama, JWT üretimi
- **user** → Kullanıcı & rol yönetimi
- **appointment** → (sonraki adım)
- **common** → Ortak yapı ve yardımcı sınıflar

---

## 🔐 Auth & Security Akışı

1. Kullanıcı `/auth/login` endpoint’ine username + password gönderir
2. Kimlik doğrulama başarılıysa:
   - JWT Access Token üretilir
3. Client her istekte:
   ```http
   Authorization: Bearer <token>
   ```
4. `JwtAuthenticationFilter` token’ı doğrular
5. `SecurityContext` içine kullanıcı bilgisi yerleştirilir
6. Role bazlı endpoint erişimi sağlanır

---

## 👤 User Modülü Yapısı

```
user
 ├── controller
 │    ├── UserController
 │    └── RoleController
 ├── service
 │    ├── UserService
 │    └── RoleService
 ├── repository
 │    ├── UserRepository
 │    └── RoleRepository
 ├── entity
 │    ├── User
 │    └── Role
 ├── dto
 │    ├── UserDto
 │    └── RoleDto
 └── mapper
      ├── UserMapper
      └── RoleMapper
```

### Katman Sorumlulukları

- **Controller** → HTTP istekleri
- **Service** → İş kuralları
- **Repository** → Veritabanı işlemleri
- **Entity** → JPA varlıkları
- **DTO** → API veri taşıma
- **Mapper** → Entity ↔ DTO dönüşümü

---

## 🧪 Test Stratejisi

Proje test yazımına %100 hazırdır.

Planlanan testler:

- **Controller Testleri** (MockMvc)
- **Service Unit Testleri** (Mockito)
- **Security Testleri** (JWT + Role)

---

## 🛠️ Kullanılan Teknolojiler

- Java 17
- Spring Boot 3.x
- Spring Security
- JWT (Access Token)
- Spring Data JPA (Hibernate)
- PostgreSQL
- Lombok
- MapStruct
- JUnit 5
- Mockito
- MockMvc

---

## 📌 Örnek Endpoint’ler

### Auth
- `POST /auth/register`
- `POST /auth/login`

### User
- `GET /users/me`
- `GET /users/{id}`

---

## 🚀 Yol Haritası

1. ✅ Auth + User mimarisi
2. ⏳ Auth + User testleri
3. ⏳ Appointment modülü
4. ⏳ Global Exception & Validation

---

## 🧠 Not

Bu proje, **öğretici olmasının yanında**, mülakatlarda ve gerçek projelerde referans olarak kullanılabilecek şekilde tasarlanmıştır.

