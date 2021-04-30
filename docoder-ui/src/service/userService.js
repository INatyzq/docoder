
//用户详情
let userService = {
  getUserDetail:function (){
    let userStr = localStorage.getItem("userDetail");
    return userStr?JSON.parse(userStr):{};
  }
};

export default userService;
