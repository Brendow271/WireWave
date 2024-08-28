<template>
  <Navbar/>
  <div>
    <ProductCard/>
  </div>
</template>

<script>
  import Navbar from '../components/MainNavbar.vue'
  import ProductCard from "../components/ProductCard.vue";
  export default {
    components: {ProductCard, Navbar },
    name: 'ProductListing'
  }

</script>