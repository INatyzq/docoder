// axios
import axios from 'axios'
import notify from "./core/notify/notify";
import router from "./router";
import appVue from "./main";
import userService from "@/service/userService";

const baseURL = "/api";

let axiosService = axios.create({
  baseURL: baseURL,
  withCredentials: true,
  // You can add your headers here
  headers: {
    'Content-Type': 'application/json',
    'web_token': userService.getUserDetail().token
  }
});

// request拦截器
axiosService.interceptors.request.use(
  config => {
    config.headers['web_token'] = userService.getUserDetail().token// 让每个请求携带自定义token 请根据实际情况自行修改
    return config
  },
  error => {
    console.log(error)
    Promise.reject(error)
  }
)

axiosService.interceptors.response.use(
  success => {
    appVue.$vs.loading.close();
    let responseData = success.data;
    if (responseData.success) {
      return responseData;
    } else {
      if (!responseData['isHandle']) {
        notify.danger(responseData.message);
        if (responseData.code == 404) {
          router.push({path: '/app/error-404'});
        } else {
          router.push({path: '/app/error-500'});
        }
        return responseData;
      } else {
        return responseData;
      }
    }
  }, error => {
    appVue.$vs.loading.close();
    if (error) {
      notify.danger('系统提示', error);
      router.push({path: '/pages/err500'})
    }
  })

export default axiosService;
