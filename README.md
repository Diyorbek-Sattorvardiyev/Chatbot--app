![image](https://github.com/user-attachments/assets/e8ed28c6-d68f-4a66-bd3b-e13eb33b7645) ![image](https://github.com/user-attachments/assets/f68b7143-db63-4de7-a998-7b7024021eb9)

# Android Chatbot App (Gemini API asosida)

Ushbu Android ilova foydalanuvchidan matn yoki ovozli xabar qabul qiladi va Google Gemini API orqali chatbot javoblarini qaytaradi. Ilova o‘zbek tilida matnni ovoz chiqarib o‘qib berish va ovozdan matn kiritish funksiyalariga ega.

## Asosiy funksiyalar

- Foydalanuvchi matn yoki ovozli so‘rov yuborishi mumkin
- Google Gemini (PaLM 2) API orqali javob olinadi
- Chat tarzida ko‘rinish
- "Yozmoqda..." animatsiyasi
- Botning javobini ustiga bosgandagina ovozda o‘qib beradi (TextToSpeech)
- Ovozdan matn kiritish (SpeechRecognizer)

## Texnologiyalar

- Java (Android SDK)
- Gemini API (Google Generative Language API)
- TextToSpeech (Android)
- SpeechRecognizer (Android)
- RecyclerView (chat ko‘rinish)
- AsyncTask (API so‘rovlar)

## API kaliti

Ilova ishlashi uchun sizda quyidagilar bo‘lishi kerak:

1. [Google AI Studio](https://aistudio.google.com/app/apikey) orqali `Gemini API Key` oling
2. `res/values/strings.xml` faylga quyidagicha joylashtiring:

```xml
<string name="gemini_api_key">YOUR_API_KEY_HERE</string>
