# Mikroservis mimarisi (ilk dilim)

Geçiş, mevcut `diyetisyen-projesi/` modüler monolitini çalışır halde tutarak `services/` altında bağımsız servisler çıkarma şeklinde ilerliyor.

## Servis sınırları

- `auth-service`: kullanıcı kimliği, giriş/kayıt, roller ve JWT. Kendi `auth_db` veritabanına sahiptir.
- `customer-service`: müşteri profili ve iletişim bilgileri; `customer_db` sahibi olur. Profil, auth servisinden gelen JWT içindeki kullanıcı ID'sine bağlanır; auth tablolarına FK kurulmaz.
- `appointment-service`: randevu yaşam döngüsü; `appointment_db` sahibi olur. Auth kullanıcısını referans olarak tutar, diğer servis tablolarına doğrudan bağlanmaz. Durum değişikliği ve outbox kaydı aynı transaction içinde yazılır; arka plan publisher'ı olayları RabbitMQ topic exchange'e iletir.
- `diet-plan-service`: beslenme planları ve öğünler; `diet_plan_db` sahibi olur.
- `notification-service`: randevu olaylarını RabbitMQ'dan tüketir, event ID ile tekrarları engeller ve SMTP yapılandırılmışsa e-posta gönderir. Kendi `notification_db` veritabanına sahiptir.

Servisler birbirlerinin veritabanlarına bağlanmaz. Her servis JWT'yi ortak imza anahtarıyla doğrular. Şu an asenkron randevu bildirimleri için RabbitMQ kullanılır; gerektiğinde servisler arası senkron sorgular HTTP API üzerinden eklenir.

## Bu dilim

Beş backend domaininin her biri `services/` altında bağımsız Spring Boot uygulaması ve ayrı PostgreSQL veritabanıyla tanımlanmıştır. Appointment olayları RabbitMQ üzerinden notification servisine gider. SMTP devre dışıysa olaylar `WAITING_CONFIGURATION` olarak kaydedilir; SMTP ayarlarını `.env` dosyasında açabilirsiniz.

## Yerel başlatma

`.env.example` dosyasını `.env` olarak kopyalayıp `JWT_SECRET` değerini en az 32 baytlık rastgele bir değerle değiştirin. Ardından repo kökünde `docker compose up --build` çalıştırın. Arayüz `http://localhost:3000`, API servisleri 8080–8084 portlarında açılır. Yerel geliştirme parolaları üretim için kullanılmamalıdır.

İlk admin hesabı gerekiyorsa `BOOTSTRAP_ADMIN_USERNAME`, `BOOTSTRAP_ADMIN_EMAIL` ve `BOOTSTRAP_ADMIN_PASSWORD` değerlerinin üçünü de verin. Hesap yalnızca başlangıçta aynı kullanıcı adı yoksa oluşturulur; bu değerleri repoya eklemeyin.

Backend modülleri `services/pom.xml` Maven aggregator'ı üzerinden birlikte derlenebilir ve test edilebilir.
