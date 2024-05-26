# microservice-test
 Berikut untuk collection postmant : [Test MSIG.postman_collection.json](https://github.com/riskigalangsaputra/microservice-test/files/15448228/Test.MSIG.postman_collection.json)

 running semua service kecuali config-service, tidak dijalankan karena terkait credentials git.

 dan pastikan redis dan database postgres sudah running, berikut gambar untuk port yang di gunakan di configurasi service : 

![image](https://github.com/riskigalangsaputra/microservice-test/assets/32057963/5cc87f84-066c-4617-93d2-f42e88a95753)

configurasi database hanya ada di user-service.
redis saya gunakan untuk publish / subcribe untuk kebutuhan kirim email.
service sebagai publish adalah authentification-service, untuk subcribe nya adalah email-service.

Noted : design saya lebih ke focus konsep atau design microservice nya, tidak mengimplement untuk case atau tema "sekolah" seperti yang di ujikan. 
        Dan masih banyak yang harus di improve lagi dari design maupun source code.

Kendala : untuk waktu dan focus pekerjaan masih terbagi - bagi.



