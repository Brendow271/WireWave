<template>
  <Navbar/>
  <CategoryInfo/>
</template>

<script>
import Navbar from '../components/MainNavbar.vue'
import CategoryInfo from "../components/CategoryInfo.vue";
export default {
  components: {CategoryInfo, Navbar },
  name: 'HomePage'
}
</script>

