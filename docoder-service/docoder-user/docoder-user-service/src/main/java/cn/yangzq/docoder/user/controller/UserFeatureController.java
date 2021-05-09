package cn.yangzq.docoder.user.controller;


import cn.yangzq.docoder.common.core.utils.ResultVo;
import cn.yangzq.docoder.user.entity.UserFeature;
import cn.yangzq.docoder.user.service.UserFeatureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
*@author yangzq
*@description 用户特性表 前端控制器
*/
@Api(tags = "用户特性接口")
@RestController
@RequestMapping("/user/feature")
public class UserFeatureController {

    @Autowired
    private UserFeatureService featureService;

    @ApiOperation("通过userId获取数据")
    @GetMapping("/byUserId/{userId}")
    public ResultVo<UserFeature> getByUserId(@PathVariable Integer userId){
        return ResultVo.success(featureService.getByUserId(userId));
    }

    @ApiOperation("通过userId获取数据")
    @PostMapping
    public ResultVo<UserFeature> saveOrUpdate(@RequestBody UserFeature feature){
        featureService.saveOrUpdate(feature);
        return ResultVo.success(feature);
    }

}

