package com.lh.oa.module.bpm.dal.dataobject.businessForm.invoicingApply;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* 开票申请单
*
* @author tanghanlin
* @since 2023-10-21
*/
@Getter
@Setter
@Accessors(chain = true)
@TableName("bpm_invoicing_apply_form")
public class BpmInvoicingApplyForm extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 申请人
     */
    private Long applyUserId;

    /**
     * 是否关联销售员
     */
    private Boolean relatedSale;

    /**
     * 相关销售
     */
    private Long relatedSaleUserId;

    /**
     * 是否收款
     */
    private Boolean received;

    /**
     * 开票公司，字段取invoicing_apply_company字典的value
     */
    private Integer applyCompany;

    /**
     * 开票种类，字段取invoicing_apply_type字典的value
     */
    private Integer formType;

    /**
     * 开票金额
     */
    private BigDecimal totalAmount;

    /**
     * 收款详细情况
     */
    private String receivedDetail;

    /**
     * 购买方名称
     */
    private String purchaseName;

    /**
     * 纳税人识别号
     */
    private String purchaseTaxpayerCode;

    /**
     * 地址、电话
     */
    private String purchaseAddressMobile;

    /**
     * 开户行
     */
    private String purchaseBank;

    /**
     * 银行账号
     */
    private String purchaseBankAccount;

    /**
     * 销售种类，字段取自invoicing_apply_sale_type字典的value
     */
    private Integer saleType;

    /**
     * 销售合同url
     */
    private String saleContractUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 附件url
     */
    private String annexUrl;

    /**
     * 流程定义id
     */
    private String processInstanceId;

    /**
     * 流程执行结果
     */
    private Integer result;

}
