# ksd-android-OCR-Reader
## 1. UI

<img src="https://user-images.githubusercontent.com/16772440/107788869-3ebb9400-6d94-11eb-8f49-6c67e4b25736.jpg" alt="UI" style="zoom:33%;" />



#### 1) 이미지 선택

해당 구역 터치 시 이미지를 선택할 수 있고 선택후 선택한 이미지가 나타납니다

#### 2) REST CALL 요청

선택한 이미지를 S3에 업로드 후 서버에 REST CALL을 요청하고 그 결과를  3)에 보여줍니다

#### 3) 결과 창

OCR 분석 결과를 보여줍니다. REST Call 실패 시 False가 나타납니다

 



## 2. 프로그램 구조
<img src="https://user-images.githubusercontent.com/16772440/107788866-3d8a6700-6d94-11eb-8b7c-3edda929a734.PNG" alt="Class struct" style="zoom:33%;" />

![](C:\Users\HAI-NLP\Desktop\2.PNG)



## 3. 설치 시 필요한 것

앱 설치 후 설정에서 **권한 - 저장공간 - 허용** 이 필요합니다 
