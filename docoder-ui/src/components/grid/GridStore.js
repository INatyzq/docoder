export class GridStore {

  static build(originalRecords){
    let gridHelper = new GridStore();
    if(Array.prototype.isPrototypeOf(originalRecords) && originalRecords.length>0){
      gridHelper.originalRecords = originalRecords;
      originalRecords.forEach(function (data) {
        let newData = {};
        for(let key in data){
          newData[key] = data[key];
        }
        gridHelper.originalRecordBak.push(newData);
      })
    }
    return gridHelper;
  }

  originalRecords = null;

  originalRecordBak = [];

  modifyRecords = [];

  newRecords = [];

  add(record){
    this.newRecords.push(record)
  }

  update(record){
    this.modifyRecords.push(record);
  }
}
