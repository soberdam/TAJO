<template>
  <div class="_main">
    
    <div class="mainbox">
      <div class="box-left">
        <span class="게시판이름">{{courseName}} 게시판</span>
        <div class="courseinfo">
          <RouterLink class="여기에두자" to="/Together">
            <button class="뒤로">&lt;</button>
          </RouterLink>
          
          <img :src="'/src/assets/'+courseImg">
          <button class="파티만들기" @click="showMakeToggle">파티만들기</button>
        </div>
        <div class="tablebox">
          <table>
            <thead>
              <tr>
                <th>제목</th>
                <th>코스</th>
                <th>파티장</th>
                <th>인원</th>
                <th>일시</th>
                <th>go?</th>
              </tr>
            </thead>
            <tbody>
            
              <tr v-for="group in groupList" :key="group.groupid">
                <td>{{ group.content }}</td>
                <td>{{ courseName }}</td>
                <td>{{ group.writer }}</td>
                <td>{{ group.people}} / {{ group.limit }}</td>
                <td>{{ group.date }}</td>
                <td v-if="group.people!=group.limit"> <!--여기서 그룹에 참여한 인원 수와 limit을 비교하여 마감/입장 나누고 싶다..-->
                  <button @click="toggleDetail(group.groupid, group.content)">파티 입장</button>
                </td>
                <td v-else>
                  <button class="파티만들기">모집 마감</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="box-right" v-show='showDetail'>
        <TogetherDetailmore :groupid="selectedGroupId" :name="selectedName" v-show='showDetail' @close-toggle="closeToggle" />
      </div>
      <div class="box-right" v-show='showMake'>
        <TogetherPartyMake v-show='showMake' @close-toggle2="closeToggle2" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref,onMounted } from "vue";
import TogetherDetailmore from "@/components/같이타조/TogetherDetailmore.vue";
import TogetherPartyMake from "./TogetherPartyMake.vue";

import { useGroupStore } from "../../stores/group";
import {useCourseStore} from '@/stores/course';
import { useRoute } from "vue-router";

const groupStore = useGroupStore();
const courseStore = useCourseStore();
const groupList = ref([]);
const courseName = ref('');
const courseImg = ref('');
const route = useRoute();
const selectedName = ref('');
const closeToggle = function () {
  showDetail.value = false;

}
const closeToggle2 = function () {
  showMake.value = false;

}
const showMakeToggle = function () {
  showMake.value = !showMake.value;
  if (showDetail.value) {
    showDetail.value = !showDetail.value
  }
}
const showDetail = ref(false);
const showMake = ref(false);
const selectedGroupId = ref(null);
const course = ref(null);

const toggleDetail = async (groupid, content) => {
  selectedName.value = content;
  selectedGroupId.value = groupid;
  await groupStore.getMemberList(groupid);
  console.log(groupStore.memberList);
  showDetail.value = !showDetail.value;

};

onMounted(async() => {
    try {
        groupList.value = await groupStore.getGroupList(route.params.courseid);
        course.value = (await courseStore.getCourse(route.params.courseid));
        courseName.value = course.value.name;
        courseImg.value = course.value.img;
        
    } catch (error){
        console.log(error);
    }
    
  
    // watch( () => [courseStore.courseList], async  ([newList]) => {
    //     coList.value = newList;
    //     console.log(courseStore.courseList)
    //     // course.value = await courseStore.getCourse(Route.params.courseid);
    // });

});
</script>

<style scoped>
.게시판이름{
  margin-bottom: 60px;
  font-size: 25px;
}
.디테일 {
  background-color: white;
  /* 배경색을 흰색 또는 다른 적절한 색상으로 설정하세요 */
}
.여기에두자{
  padding-right: 50px;

}
.뒤로 {
    background-color: rgb(218, 238, 255);
    padding-top: 3px;
    padding-bottom: 3px;
}
img{
  min-width: 400px;
  min-height: 240px;
  border: 4px solid black;
}
.courseinfo {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 730px;
  margin-bottom: 30px;
}

table {
  width: 800px;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: center;
}

th {
  background-color: #ffe4d6;
}

.box-right {
  position: static;
  width: 25%;
  right: 0px;
  top: 80px;
  bottom: 130px;
  height: 108%;
  background-color: white;
  border-left: solid 2px #7f7f7f;
}

.box-left {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 72%;
}

img {
  width: 210px;
  height: 160px;
}

._main {
  width: 99.7%;
  height: 720px;
  display: flex;
  justify-content: center;
  align-items: center;
}
button {
    background-color: #ffd8d5;
    font-family: 'cookierun';
    color: rgb(0, 0, 0);
    border: 1px solid black;
    padding: 4px 7px;
    cursor: pointer;
    border-radius: 3px;
}
.mainbox {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80%;
  height: 550px;
}
.파티만들기{
  background-color: rgb(213, 233, 251);
}
</style>