package lc.activiti.dao.base;

import lc.activiti.entity.RecruitPlanBase;

public interface IRecruitPlanBaseDao {
    /**
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String recruitPlanId);

    /**
     *
     * @mbg.generated
     */
    int insert(RecruitPlanBase record);

    /**
     *
     * @mbg.generated
     */
    int insertSelective(RecruitPlanBase record);

    /**
     *
     * @mbg.generated
     */
    RecruitPlanBase selectByPrimaryKey(String recruitPlanId);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(RecruitPlanBase record);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(RecruitPlanBase record);
}