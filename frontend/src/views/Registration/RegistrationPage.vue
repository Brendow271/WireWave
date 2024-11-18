<template>
  <div>
    <Navbar/>
    <div class="border-solid d-flex flex-column rounded ma-5">
      <h1 class="d-flex justify-center">Регистрация</h1>
      <v-text-field
          v-model="form.firstName"
          variant="outlined"
          placeholder="Имя"
          class="mx-auto justify-center registration-field"
      />
      <v-text-field
          v-model="form.lastName"
          variant="outlined"
          placeholder="Фамилия"
          class="mx-auto justify-center registration-field"
      />
      <v-text-field
          v-model="form.email"
          variant="outlined"
          placeholder="Email"
          class="mx-auto justify-center registration-field"
      />
      <v-text-field
          v-model="form.password"
          type="password"
          variant="outlined"
          placeholder="Пароль"
          class="mx-auto justify-center registration-field"
      />
      <v-text-field
          v-model="form.confirmPassword"
          type="password"
          variant="outlined"
          placeholder="Повторите пароль"
          class="mx-auto justify-center registration-field"
      />
      <v-btn @click="registerUser" class="mx-auto justify-center" color="primary">
        Зарегистрироваться
      </v-btn>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import Navbar from '../../components/MainNavbar.vue';

export default {
  components: { Navbar },
  name: 'RegistrationPage',
  data() {
    return {
      form: {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        confirmPassword: ''
      }
    }
  },
  methods: {
    async registerUser() {

      if (!this.form.firstName || !this.form.lastName || !this.form.email || !this.form.password || this.form.password !== this.form.confirmPassword) {
        alert('Пожалуйста, заполните все поля корректно!');
        return;
      }

      try {
        const response = await axios.post('https://your-backend-url/api/register', this.form);

        // Если запрос прошел успешно
        alert('Регистрация успешна!');
        this.$router.push('/');
      } catch (error) {
        // Если произошла ошибка
        console.error('Ошибка регистрации:', error.response?.data?.message || error.message);
        alert(error.response?.data?.message || 'Ошибка при регистрации. Попробуйте позже.');
      }
    }
  }
}
</script>
