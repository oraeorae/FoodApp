package com.example.spring_food.test;

import com.example.spring_food.pojo.UserActiveDTO;
import com.example.spring_food.pojo.UserSimilarityDTO;
import com.example.spring_food.service.RecommendService;
import com.example.spring_food.service.UserSimilarityService;
import com.example.spring_food.utils.RecommendUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author czh
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommendTest {

    @Autowired
    private RecommendService recommendService;


    /**
     * 测试更新或者插入用户数据
     * 收藏和浏览都应该执行这个
     */
    @Test
    public void testUpdate(){
        int userid = 6;
        int businessid = 10;
        //判断当前有没有该用户的商店数据
        if( recommendService.isUserActive(userid,businessid) == 0 ){        //没有数据则插入
            recommendService.insertUserActive(userid,businessid);
        }else{          //已经存在则更新
            recommendService.updateUserActive(userid,businessid);
        }
    }

    /**
     * 测试列出所有用户的购买行为的方法
     */
    @Test
    public void testListAllUserActive() {
         // 1.查询出所有用户对所有二级类目的浏览记录
        List<UserActiveDTO> userActiveDTOList = recommendService.listUserActive();

        // 2.输出浏览记录列表
        for (UserActiveDTO userActiveDTO : userActiveDTOList) {
            System.out.println(userActiveDTO.getUserId() + "\t" + userActiveDTO.getBusinessId() + "\t" + userActiveDTO.getHits());
        }
    }

    /**
     * 测试组装用户行为数据的方法
     */
    @Test
    public void testAssembleUserBehavior() {
        // 1.查询所有的用户浏览记录
        List<UserActiveDTO> userActiveDTOList = recommendService.listUserActive();

        // 2.调用推荐模块工具类的方法组装成一个ConcurrentHashMap来存储每个用户以及其对应的二级类目的点击量
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap = RecommendUtils.assembleUserBehavior(userActiveDTOList);

        // 3.输出封装后的map的大小（也就是多少个用户的浏览记录）
        System.out.println(activeMap.size());
    }

    @Autowired
    UserSimilarityService userSimilarityService;
    /**
     * 计算用户的相似度
     */
    @Test
    public void testCalcSimilarityBetweenUser() {
       // 1.查询所有的用户浏览记录
        List<UserActiveDTO> userActiveDTOList = recommendService.listUserActive();

        // 2.调用推荐模块工具类的方法组装成一个ConcurrentHashMap来存储每个用户以及其对应的二级类目的点击量
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap = RecommendUtils.assembleUserBehavior(userActiveDTOList);

        // 3.调用推荐模块工具类的方法计算用户与用户之间的相似度
        List<UserSimilarityDTO> similarityList = RecommendUtils.calcSimilarityBetweenUsers(activeMap);

        // 4.输出计算好的用户之间的相似度
        for (UserSimilarityDTO usim : similarityList) {
            System.out.println(usim.getUserId() + "\t" + usim.getUserRefId() + "\t" + usim.getSimilarity());
            // 5.如果用户之间的相似度已经存在与数据库中就修改，不存在就添加
            if (userSimilarityService.isExistsUserSimilarity(usim) > 0 ) { // 修改
                int flag = userSimilarityService.updateUserSimilarity(usim);
                if ( flag == 1 ) {
                    System.out.println("修改数据成功");
                }
            } else { // 新增
                int flag = userSimilarityService.saveUserSimilarity(usim);
                if ( flag == 1 ) {
                    System.out.println("插入数据成功");
                }
            }
        }

    }


    /**
     * 测试查询用户相似度集合列表
     */
    @Test
    public void testListUserSimilarity() {
        // 1.查询出某个用户与其他用户的相似度列表
        List<UserSimilarityDTO> userSimilarityList = userSimilarityService.listUserSimilarityByUId(1L);

        // 2.打印输出
        for (UserSimilarityDTO userSimilarityDTO : userSimilarityList) {
            System.out.println(userSimilarityDTO.getUserId() + "\t" + userSimilarityDTO.getUserRefId() + "\t" + userSimilarityDTO.getSimilarity());
        }

    }


    /**
     * 测试取出与指定用户相似度最高的前N个用户
     */
    @Test
    public void testGetTopNUser() {

       // 1.查询出某个用户与其他用户的相似度列表
        List<UserSimilarityDTO> userSimilarityList = userSimilarityService.listUserSimilarityByUId(1L);

        // 2.打印输出
        for (UserSimilarityDTO userSimilarityDTO : userSimilarityList) {
            System.out.println(userSimilarityDTO.getUserId() + "\t" + userSimilarityDTO.getUserRefId() + "\t" + userSimilarityDTO.getSimilarity());
        }

        // 3.获取与id为2L的用户的浏览行为最相似的前2个用户
        List<Long> userIds = RecommendUtils.getSimilarityBetweenUsers(1L, userSimilarityList, 3);

        // 4.打印输出
        System.out.println("与" + 1 + "号用户最相似的前3个用户为：");
        for (Long userRefId : userIds) {
            System.out.println(userRefId);
        }

    }

    /**
     * 获取被推荐的类目id列表
     */
    @Test
    public void testGetRecommendateCategoy2() {
        // 1.查询出某个用户与其他用户的相似度列表
        List<UserSimilarityDTO> userSimilarityList = userSimilarityService.listUserSimilarityByUId(1L);

        // 2.获取所有的用户的浏览记录
        List<UserActiveDTO> userActiveList = recommendService.listUserActive();
        for (UserSimilarityDTO userSimilarityDTO : userSimilarityList) {
            System.out.println(userSimilarityDTO.getUserId() + "\t" + userSimilarityDTO.getUserRefId() + "\t" + userSimilarityDTO.getSimilarity());
        }

        // 3.找出与id为1L的用户浏览行为最相似的前3个用户
        List<Long> userIds = RecommendUtils.getSimilarityBetweenUsers(1L, userSimilarityList, 3);
        System.out.println("与" + 1 + "号用户最相似的前3个用户为：");
        for (Long userRefId : userIds) {
            System.out.println(userRefId);
        }

        // 4.获取应该推荐给1L用户的二级类目
        List<Long> recommendateBusiness = RecommendUtils.getRecommendateBusiness(1L, userIds, userActiveList);
        for (Long businessId : recommendateBusiness) {
            System.out.println("被推荐的商店类目是：" + businessId);
        }

    }

}
