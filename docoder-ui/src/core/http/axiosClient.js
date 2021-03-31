import axios from "../../axios";

export const postRequest = (url,data) => {
  return axios.post(url,data);
}
// put请求封装
export const putRequest = (url, params) => {
  return axios.put(url,params);
}
// get请求封装
export const getRequest = (url, params) => {
  return axios.get(url,{params:params})
}
export const getRequestX = (url, params) => {
  return axios.get(url,params)
}
// delete请求封装
export const deleteRequest = (url, params) => {
  return axios.delete(url,params)
}
