<template>
    <div>
      <div class="맵밑에">
        <span>장소 검색기</span>
        <div>
          <input v-model="searchKeyword" placeholder="장소 검색어를 입력하세요">
          <button @click="searchPlaces">검색</button>
        </div>
      </div>
      <div id="map"></div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  
  let map = null;
  let infowindow = null;
  let ps = null;
  const searchKeyword = ref('');
  let markers = [];
  
  const initMap = () => {
    const container = document.getElementById('map');
    const options = {
      center: new kakao.maps.LatLng(37.566826, 126.9786567),
      level: 7,
    };
    map = new kakao.maps.Map(container, options);
  
    infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
    ps = new kakao.maps.services.Places(map);
  
    // 사용자의 현재 위치를 얻어오는 함수 호출
    getCurrentLocation();
  };
  
  // 사용자의 현재 위치를 얻어오는 함수
  const getCurrentLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const lat = position.coords.latitude;
          const lng = position.coords.longitude;
          const currentLocation = new kakao.maps.LatLng(lat, lng);
  
          // 지도의 중심을 현재 위치로 이동
          map.setCenter(currentLocation);
  
          // 장소 검색 서비스 호출 (주변 검색)
          ps.categorySearch('PM9', placesSearchCB, { useMapBounds: true });
        },
        (error) => {
          console.error('Error getting user location:', error);
        }
      );
    } else {
      console.error('Geolocation is not supported by this browser.');
    }
  };
  
  function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
      for (let i = 0; i < data.length; i++) {
        displayMarker(data[i]);
      }
    }
  }
  
  function displayMarker(place) {
    const marker = new kakao.maps.Marker({
      map: map,
      position: new kakao.maps.LatLng(place.y, place.x),
    });
  
    markers.push(marker);
  
    kakao.maps.event.addListener(marker, 'click', function () {
      infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
      infowindow.open(map, marker);
    });
  }
  
  function removeMarkers() {
    for (let i = 0; i < markers.length; i++) {
      markers[i].setMap(null);
    }
    markers = [];
  }
  
  const searchPlaces = () => {
    removeMarkers();
    ps.keywordSearch(searchKeyword.value, placesSearchCB, { useMapBounds: true });
  };
  
  onMounted(async () => {
    if (window.kakao && window.kakao.maps) {
      initMap();
    } else {
      const script = document.createElement('script');
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=4db16c8d32c6e47d1849987c33d4cc9c&libraries=services`;
      document.head.appendChild(script);
      script.onload = () => {
        kakao.maps.load(initMap);
      };
    }
  });
  </script>
  
  
  <style scoped>
  .맵밑에 {
    width: 350px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    margin-top: 7px;
    
  }
  
  #map {
    margin: 10px 20px 20px 20px;
    width: 1130px;
    height: 550px;
  }
  
  /* input {
    padding: 5px;
  }
  
  button {
    padding: 5px;
  } */
  </style>
  