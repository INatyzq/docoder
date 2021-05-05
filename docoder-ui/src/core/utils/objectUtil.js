let objectUtil = {

  reset: function (obj) {
    for (let key in obj) {
      if (obj.hasOwnProperty(key)) {
        obj[key] = null;
      }
    }
  }

}

export default objectUtil;

