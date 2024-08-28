<template>
  <div class="ma-5">
    <v-card
        variant="outlined"
        class="d-flex"
    >
      <v-img
          src="../../public/images/imageTest.jpeg"
          alt="Some product"
          width="200"
      />
      <div>
        <v-card-title>
          Название
        </v-card-title>
        <v-card-text>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
        </v-card-text>
      </div>
      <v-card-actions>
        <div>
          <v-card-text>
            <v-avatar class="small"
                      size="15"
                      color="success"
            />
            В наличии
          </v-card-text>
          <v-card-text class="d-flex justify-center">
            12499руб.
          </v-card-text>
          <v-btn
              variant="outlined"
              text="Добавить в корзину"
          />
        </div>
      </v-card-actions>
    </v-card>
  </div>
</template>


<script>
export default {
  name: 'ProductCard',
  data() {
    return {

    }
  }
}
</script>