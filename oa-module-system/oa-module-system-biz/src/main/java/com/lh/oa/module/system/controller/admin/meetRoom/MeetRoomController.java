package com.lh.oa.module.system.controller.admin.meetRoom;

import com.lh.oa.framework.common.pojo.JsonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.meetRoom.MeetRoom;
import com.lh.oa.module.system.dal.dataobject.meetRoom.MeetRoomQuery;
import com.lh.oa.module.system.service.meetRoom.IMeetRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/system/meetRoom")
@Validated
public class MeetRoomController {

    @Autowired
    public IMeetRoomService meetRoomService;

    /**
    * 添加会议室
    */
    @RequestMapping(value="/create",method= RequestMethod.POST)
    public JsonResult save(@Valid @RequestBody MeetRoom meetRoom){
        meetRoomService.insert(meetRoom);
        return JsonResult.success();
    }


    /**
     * 添加会议室
     */
    @RequestMapping(value="/update",method= RequestMethod.PUT)
    public JsonResult update(@Valid @RequestBody MeetRoom meetRoom){
        meetRoomService.updateById(meetRoom);
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/delete",method=RequestMethod.DELETE)
    public JsonResult delete(@RequestParam("id") Long id){
        meetRoomService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public JsonResult get(@RequestParam("id")Long id){
        return JsonResult.success(meetRoomService.selectById(id));
    }


    /**
    * 查询所有会议室
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(meetRoomService.selectList());
    }





    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public JsonResult page(@Valid MeetRoomQuery query){


        PageResult<MeetRoom> page = meetRoomService.selectPage(query);
        return JsonResult.success(page);
    }
}
