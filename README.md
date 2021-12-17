# ff4gogo

---

[![Build Status](https://app.travis-ci.com/pds0309/ff4gogo.svg?branch=master)](https://app.travis-ci.com/pds0309/ff4gogo) ![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/pds0309/ff4gogo)
<a href='https://coveralls.io/github/pds0309/ff4gogo?branch=master'><img src='https://coveralls.io/repos/github/pds0309/ff4gogo/badge.svg?branch=master&kill_cache=1' alt='Coverage Status' /></a>
<a href='https://img.shields.io/badge/springboot-2.4.5-orange'><img src='https://img.shields.io/badge/springboot-2.4.5-orange' /></a>


----

### 피파온라인4 open-api 를 활용한 유저순위경기 조회 서비스

<br>

#### 서비스 소개


NEXONDEVELOPERS 에서 제공되는 피파온라인4 open-api 를 활용한 게임 이용자 순위경기 전적 조회 서비스

랭크 경기 기록에 대한 데이터들을 보다 시각적으로 보여주어 사용자가 더 나은 인사이트를 빠르게 얻고 가볍게 접속해 조회할 수 있도록 구성


<br>

#### 서비스 전체 흐름


![f4gogoarc](https://user-images.githubusercontent.com/76927397/142144575-c38f513a-d415-43f5-ac98-e579e126e0fd.PNG)

<br>

#### 주요 기능

* 메인페이지에서 랭커들의 TOP 선수들 확인
  * 일정 시간 마다 스케줄링으로 업데이트 되는 데이터를 조회하는 api이기 때문에 웹 서버 캐싱 적용

<img src="https://user-images.githubusercontent.com/76927397/218298218-695d0d61-a833-468b-baa0-69612814afb3.JPG" width=700/>

* 게임 이용자 전적 검색 및 조회
  * 해당 사용자 최근 요약 기록, 전적 목록 조회
  * 전적 상세 조회, 기대득점 모델링 api를 활용해 경기 기대득점 시각화하여 제공


<img src="https://user-images.githubusercontent.com/76927397/218298687-9c5e1ac4-05b3-4a61-b7de-4cc0ea30dd99.gif" width=50%/>

<img src="https://user-images.githubusercontent.com/76927397/218298701-54d4774a-b00f-4cf5-bf85-7efb47edaaee.gif" width=50%/>




#### 주요 이슈

* [서버설정 기록 #23](https://github.com/pds0309/ff4gogo/issues/23)

* [기계학습 모델을 통해 기대득점 수치 보여주기 #21](https://github.com/pds0309/ff4gogo/issues/21)


##### 기타

* 개발도구
  * Intellij ultimate
* 빌드, 테스트, 배포
  * TravisCI, AWS CodeDeploy, S3

  
##### Back
  
* 웹서버 : nginx
  

* Language
  * Java 8
* Framework
  * SpringBoot 2.4.5
* Build Tool
  * gradle 6.8.3
* Test
  * Junit5
* ORM
  * JPA
   
* DBMS
  * 개발 : H2
  * 배포 : Oracle Cloud ATP Database (19c)

<br> 

##### Front

* HTML5
* Css 
  * [bulma](https://bulma.io/) framework
* JavaScript 
    * [apexchart](https://apexcharts.com/)
* Jquery 3.6

