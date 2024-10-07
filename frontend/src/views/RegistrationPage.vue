<template>
  <div>
    <Navbar/>
    <div
      class="border-solid
      d-flex
      flex-column
      rounded
      ma-5
      "
    >
      <h1 class="d-flex justify-center">Регистрация</h1>
      <v-text-field
          variant="outlined"
          placeholder="Имя"
          class="mx-auto justify-center registration-field"
      />
      <v-text-field
          variant="outlined"
          placeholder="Фамилия"
          class="mx-auto justify-center registration-field"
      />
      <v-text-field
          variant="outlined"
          placeholder="Email"
          class="mx-auto justify-center registration-field"
      />
      <v-text-field
          variant="outlined"
          placeholder="Пароль"
          class="mx-auto justify-center registration-field"
      />
      <v-text-field
          variant="outlined"
          placeholder="Повторите пароль"
          class="mx-auto justify-center registration-field"
      />
    </div>
  </div>
</template>

<script>
import Navbar from '../components/MainNavbar.vue'
  export default {
    components: {Navbar},
    name: 'RegistrationPage'
  }

</script>
