# Denge — Diyetisyen yönetim sistemi

Repo, Spring Boot mikroservisleri ve React arayüzünü içerir. Servis sınırları, API akışları ve yerel başlatma ayarları için [ARCHITECTURE.md](ARCHITECTURE.md) dosyasına bakın.

## Yerel çalıştırma

1. Docker Desktop’ı açın ve Docker Engine’in hazır olduğunu doğrulayın.
2. `.env.example` dosyasını `.env` olarak kopyalayın. En az `JWT_SECRET` değerini güçlü, rastgele ve 32 bayttan uzun bir değerle değiştirin.
3. Repo kökünde `docker compose up --build` çalıştırın.
4. Arayüzü `http://localhost:3000` adresinde açın. İlk admin hesabı için `.env` içindeki `BOOTSTRAP_ADMIN_*` değerleri kullanılır.

Çalışma sırasında üretilen `.env` dosyası repoya eklenmemelidir. SMTP ayarları verilmediyse bildirim olayları kaydedilir, e-posta gönderilmez.

Backend modüllerini toplu derlemek ve test etmek için repo kökünden `diyetisyen-projesi/mvnw.cmd -f services/pom.xml test` çalıştırın. Tek servisi çalışmak için aynı wrapper'a ilgili servis `pom.xml` dosyasını verin.

`diyetisyen-projesi/` dizini eski modüler monolitin kaynaklarını geçiş süresince saklar. Yeni geliştirmeler `services/` altındaki bağımsız servislerde yapılır.
