let queryHelper = {
  current: 1,
  size: 20,
  refresh:function (cmp,methodName) {
    this.init();
    cmp[methodName]();
    cmp.$refs.filterCard.removeRefreshAnimation();
  },
  getParams:function () {
    let params = {};
    for(let key in this){
      if(typeof(this[key])!='function') {
        let val = this[key];
        params[key] = val ? val : null;
      }
    }
    return params;
  },
  init:function () {
    for(let key in this){
      if(typeof(this[key])!='function') {
        delete this[key];
      }
    }
    this.current = 1;
    this.size = 20;
  }
};
/*class QueryHelper {

  constructor(){
    this.queryParams = QueryParams;

    this.setCurrentPageNum = (currentPageNum) => this.queryParams.current = currentPageNum;

    this.setPageSize = (pageSize) => this.queryParams.size = pageSize;

    this.getParams = () => {return this.queryParams;}
  }

}
let queryHelper = new QueryHelper();*/
export default queryHelper;
