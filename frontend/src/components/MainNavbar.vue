<template>
    <v-app-bar>
      <v-app-bar-nav-icon></v-app-bar-nav-icon>
      <v-btn text icon>
        <v-icon>mdi-view-list</v-icon>
      </v-btn>
      <v-btn text>
        Каталог
      </v-btn>

      <v-spacer/>

      <v-btn text icon>
        <v-icon>mdi-account</v-icon>
      </v-btn>
      <v-btn text icon>
        <v-icon>mdi-cart</v-icon>
      </v-btn>
      <v-btn text>
        Выход
      </v-btn>
    </v-app-bar>
</template>

<script>
export default {
  name: 'MainNavbar',
  data() {
    return {

    }
  }
}
</script>
