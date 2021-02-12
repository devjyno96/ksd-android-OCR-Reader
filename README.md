# ksd-android-OCR-Reader
## 1. UI

<img src="C:\Users\HAI-NLP\Desktop\Screenshot_1613141596 - 복사본.jpg" alt="Screenshot_1613141596 - 복사본" style="zoom:33%;" />



#### 1) 이미지 선택

해당 구역 터치 시 이미지를 선택할 수 있고 선택후 선택한 이미지가 나타납니다

#### 2) REST CALL 요청

선택한 이미지를 S3에 업로드 후 서버에 REST CALL을 요청하고 그 결과를  3)에 보여줍니다

#### 3) 결과 창

OCR 분석 결과를 보여줍니다. REST Call 실패 시 False가 나타납니다

 



## 2. 프로그램 구조

![](C:\Users\HAI-NLP\Desktop\2.PNG)



## 3. 설치 시 필요한 것

앱 설치 후 설정에서 **권한 - 저장공간 - 허용** 이 필요합니다 