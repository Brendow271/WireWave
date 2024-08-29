<template>
  <div
      class="bg-white ma-3 category-style"
      @click="changeStatus()"
  >
    <v-img
        class="mx-auto pa-2 border-solid"
        src="../../public/images/imageTest.jpeg"
        alt="Example category"
        width="200"
        rounded
    />
    <h2 class="d-flex justify-center">Категория</h2>
    <div
      v-if="isOpenedCategory === null"
    >
      <v-list class="pa-0 d-flex flex-column">
        <v-list-item
            v-for="i in 3"
            :key="i.id"
            class="pa-1 mx-auto"
        >
          <div>
            Категория-№{{i}}
          </div>
        </v-list-item>
      </v-list>
    </div>
  </div>
</template>

<script>
  import {ref} from "vue";

  export default {
    name: 'CategoryInfo',
    setup(){

      const isOpenedCategory = ref(null);
      const changeStatus = () => {
        if (isOpenedCategory.value === null) {
          isOpenedCategory.value = 1;
        } else {
          isOpenedCategory.value = null;
        }
      }

      return {
        isOpenedCategory,
        changeStatus
      };
    }
  }

</script>
