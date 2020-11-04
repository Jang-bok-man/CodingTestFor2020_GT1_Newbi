## 프로그램 설명
본 프로그램은 분할된 이미지 파일 데이터를 조합하여 재조합된 이미지파일로 생성하는 기능을 수행한다. 
 * 사용언어: JAVA (jdk 1.8 이상)
 * 개발 IDE 환경: IntelliJ
 
## 주요 기능

### 이미지 데이터 리더 (ImageDataReader)
1. 분할된 이미지 파일(총 62개)을 읽어서 ImageData 객체를 반환한다.
  - 분할된 이미지 파일 포멧은 "코딩테스트_파일포맷.pptx"를 참고
   
### 메인 프로그램 
1. 이미지 데이터 리더에서 읽은 분할된 이미지를 재조합 하여 복구된 원본 이미지를 파일로 생성한다.
   
## 작업 진행 흐름
1. "Resource/Sample/" 경로에 위치한 분할된 이미지 파일을 순차적으로 읽기
2. 읽은 자료를 InageData 객체로 생성
3. 각 InageData를 Sequence Number에 맞게 재배치하여 Image Data를 병합
4. 병합된 Image Data를 이미지 파일로 저장


## 추가 점수 부여
1. Unit Test Code 작성
1. 네트워크로 분할된 이미지 데이터 수신 및 처리로 변경을 고려한 구조 구성


## 유의 사항
1. Open Source 를 이용해도 무관함. 단, 어떤 식으로 동작하는 것인지 이해해야 하며 질문 시, 설명 가능해야 함
1. 질문이 있을 시, gtl@satreci 으로 문의