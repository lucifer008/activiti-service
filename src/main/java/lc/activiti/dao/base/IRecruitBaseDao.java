package lc.activiti.dao.base;

import lc.activiti.entity.RecruitBase;

public interface IRecruitBaseDao {
    /**
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String recruitId);

    /**
     *
     * @mbg.generated
     */
    int insert(RecruitBase record);

    /**
     *
     * @mbg.generated
     */
    int insertSelective(RecruitBase record);

    /**
     *
     * @mbg.generated
     */
    RecruitBase selectByPrimaryKey(String recruitId);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(RecruitBase record);

    /**
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(RecruitBase record);
}