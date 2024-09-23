package com.lh.oa.module.system.controller.admin.orderMeet;

import com.lh.oa.framework.common.pojo.CommonResult;
import com.lh.oa.framework.common.pojo.JsonResult;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.dal.dataobject.orderMeet.*;
import com.lh.oa.module.system.service.orderMeet.IOrderMeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.lh.oa.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/system/orderMeet")
@Validated
public class OrderMeetController {

    @Autowired
    public IOrderMeetService orderMeetService;

    /**
    * 添加会议的预约
    */
    @RequestMapping(value="/create",method= RequestMethod.POST)
    public JsonResult create(@Valid @RequestBody OrderMeet orderMeet){
        orderMeetService.insert(orderMeet);
        return JsonResult.success();
    }


    /**
     * 保存和修改公用的
     */
    @RequestMapping(value="/save/update",method= RequestMethod.PUT)
    public JsonResult update(@Valid @RequestBody OrderMeet orderMeet){
        orderMeetService.updateById(orderMeet);
        return JsonResult.success();
    }

    /**
     * 取消预约
     */
    @RequestMapping(value="/cancel",method= RequestMethod.PUT)
    public JsonResult cancel(@RequestParam("id") Long id){
        orderMeetService.cancel(id);
        return JsonResult.success();
    }

    /**
     * 审核预约
     */
    @RequestMapping(value="/audit",method= RequestMethod.PUT)
    public JsonResult audit(@Valid @RequestBody AuditMeet auditMeet){
        orderMeetService.audit(auditMeet);
        return JsonResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/delete",method=RequestMethod.DELETE)
    public JsonResult delete(@RequestParam("id") Long id){
        orderMeetService.deleteById(id);
        return JsonResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public JsonResult get(@RequestParam("id")Long id){
        return JsonResult.success(orderMeetService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JsonResult list(){
        return JsonResult.success(orderMeetService.selectList());
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public JsonResult page(@Valid OrderMeetQuery query){
        PageResult<OrderMeet> page = orderMeetService.selectPage(query);
        return JsonResult.success(page);
    }


    /**
     * 获取所有待审核数据
     */
    @RequestMapping(value = "/wait",method = RequestMethod.GET)
    public JsonResult waitAudit(@Valid OrderMeetQuery query){
        PageResult<OrderMeet> page = orderMeetService.waitAudit(query);
        return JsonResult.success(page);
    }


    /**
     * 获取会议室的预约记录
     */
    @RequestMapping(value = "/getByRoomId",method = RequestMethod.GET)
    public JsonResult getByRoomId(@Valid MeetBooking meetBooking){
        return JsonResult.success(orderMeetService.getByRoomId(meetBooking));
    }
    /**
     * 根据日期获取会议室的预约记录
     */
    @GetMapping("/dayList")
    public CommonResult<List<OrderMeetDay>> getScheduleListByDay() {
        List<OrderMeetDay> orderMeetDayList = orderMeetService.getByDay();
        return success(orderMeetDayList);
    }

    /**
     * 根据会议室获取预约记录
     */
    @GetMapping("/meetRoom")
    public CommonResult<List<OrderMeetRoom>> getScheduleListByRoom() {
        List<OrderMeetRoom> orderMeetRoomList = orderMeetService.getByRoom();
        return success(orderMeetRoomList);
    }
}
