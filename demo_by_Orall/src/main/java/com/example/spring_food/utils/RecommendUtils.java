package com.example.spring_food.utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.example.spring_food.pojo.UserActiveDTO;
import com.example.spring_food.pojo.UserSimilarityDTO;


/**
 * 类描述：用于美食推荐的工具类
 * 参考：https://github.com/MrQuJL/business-recommendation-system/blob/master/shopping/src/main/java/com/lyu/shopping/recommendate/util/RecommendUtils.java
 */
public class RecommendUtils {

    /**
     * 更新用户的购买行为，最终插入数据库中
     * @param userId 用户id
     * @param itemId 美食id
     * @return true则更新成功，false则更新失败
     */
    public static boolean updateBuyingBehavior(Long userId, Long itemId) {
        boolean flag = false;
        // TODO
        return flag;
    }

    /**
     * 将用户的购买行为组装成一个map,key为userId，value也是一个map，这个map记录的是商家类目以及它对应的点击量
     * @param userActiveList 用户的购买行为列表
     * @return 组装好的用户的购买行为的map集合
     */
    public static ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> assembleUserBehavior(List<UserActiveDTO> userActiveList) {
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap = new ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>>();
        // 遍历查询到的用户点击行为数据
        for (UserActiveDTO userActive : userActiveList) {
            // 1.获取用户id
            Long userId = userActive.getUserId();
            // 2.获取商家类目id
            Long category2Id = userActive.getBusinessId();
            // 3.获取该商家类目的点击量
            Long hits = userActive.getHits();

            // 判断activeMap中是否已经存在了该userId的信息，如果存在则进行更新
            if (activeMap.containsKey(userId)) {
                ConcurrentHashMap<Long, Long> tempMap = activeMap.get(userId);
                tempMap.put(category2Id, hits);
                activeMap.put(userId, tempMap);
            } else {
                // 不存在则直接put进
                ConcurrentHashMap<Long, Long> category2Map = new ConcurrentHashMap<Long, Long>();
                category2Map.put(category2Id, hits);
                activeMap.put(userId, category2Map);
            }
        }

        return activeMap;
    }

    /**
     * 计算用户与用户之间的相似性，返回计算出的用户与用户之间的相似度对象
     * @param activeMap 用户对各个商家类目的购买行为的一个map集合
     * @return 计算出的用户与用户之间的相似度的对象存储形式
     */
    public static List<UserSimilarityDTO> calcSimilarityBetweenUsers(ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap) {
        // 用户之间的相似度对集合
        List<UserSimilarityDTO> similarityList = new ArrayList<UserSimilarityDTO>();

        // 获取所有的键的集合
        Set<Long> userSet = activeMap.keySet();

        // 把这些集合放入ArrayList中
        List<Long> userIdList = new ArrayList<Long>(userSet);

        // 小于两个说明当前map集合中只有一个map集合的购买行为，或者一个都没有，直接返回
        if (userIdList.size() < 2) {
            return similarityList;
        }

        // 计算所有的用户之间的相似度对
        for (int i = 0; i < userIdList.size() - 1; i++) {
            for (int j = i + 1; j < userIdList.size(); j++) {
                // 分别获取两个用户对每个商家类目的点击量
                ConcurrentHashMap<Long, Long> userBusinessMap = activeMap.get(userIdList.get(i));
                ConcurrentHashMap<Long, Long> userRefBusinessMap = activeMap.get(userIdList.get(j));

                // 获取两个map中商家类目id的集合
                Set<Long> key1Set = userBusinessMap.keySet();
                Set<Long> key2Set = userRefBusinessMap.keySet();
                Iterator<Long> it1 = key1Set.iterator();
                Iterator<Long> it2 = key2Set.iterator();

                // 两用户之间的相似度
                double similarity = 0.0;
                // 余弦相似度公式中的分子
                double molecule = 0.0;
                // 余弦相似度公式中的分母
                double denominator = 1.0;
                // 余弦相似度公式中分母根号下的两个向量的模的值
                double vector1 = 0.0;
                double vector2 = 0.0;

                while (it1.hasNext() && it2.hasNext()) {
                    Long it1Id = it1.next();
                    Long it2Id = it2.next();
                    // 获取商家类目对应的点击次数
                    Long hits1 = userBusinessMap.get(it1Id);
                    Long hits2 = userRefBusinessMap.get(it2Id);
                    // 累加分子
                    molecule += hits1 * hits2;
                    // 累加分母中的两个向量的模
                    vector1 += Math.pow(hits1, 2);
                    vector2 += Math.pow(hits2, 2);
                }
                // 计算分母
                denominator = Math.sqrt(vector1) * Math.sqrt(vector2);
                // 计算整体相似度
                similarity = molecule / denominator;

                // 创建用户相似度对对象
                UserSimilarityDTO userSimilarityDTO = new UserSimilarityDTO();
                userSimilarityDTO.setUserId(userIdList.get(i));
                userSimilarityDTO.setUserRefId(userIdList.get(j));
                userSimilarityDTO.setSimilarity(similarity);
                // 将计算出的用户以及用户之间的相似度对象存入list集合
                similarityList.add(userSimilarityDTO);
            }
        }

        return similarityList;
    }

    /**
     * 找出与userId购买行为最相似的topN个用户
     * @param userId 需要参考的用户id
     * @param topN 与userId相似用户的数量
     * @return 与usereId最相似的topN个用户
     */
    public static List<Long> getSimilarityBetweenUsers(Long userId, List<UserSimilarityDTO> userSimilarityDTOList, Integer topN) {
        // 用来记录与userId相似度最高的前N个用户的id
        List<Long> similarityList = new ArrayList<Long>(topN);

        // 堆排序找出最高的前N个用户，建立小根堆，遍历的时候当前的这个相似度比堆顶元素大就剔掉堆顶的值，把这个数入堆(把小的都删除干净,所以要建立小根堆)
        PriorityQueue<UserSimilarityDTO> minHeap = new PriorityQueue<UserSimilarityDTO>(new Comparator<UserSimilarityDTO>(){
            @Override
            public int compare(UserSimilarityDTO o1, UserSimilarityDTO o2) {
                if (o1.getSimilarity() - o2.getSimilarity() > 0) {
                    return 1;
                } else if (o1.getSimilarity() - o2.getSimilarity() == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        for (UserSimilarityDTO userSimilarityDTO : userSimilarityDTOList) {
            if (minHeap.size() < topN) {
                minHeap.offer(userSimilarityDTO);
                System.out.println(minHeap.peek().getSimilarity());
            } else if (minHeap.peek().getSimilarity() < userSimilarityDTO.getSimilarity()) {
                minHeap.poll();
                minHeap.offer(userSimilarityDTO);
            }
        }
        // 把得到的最大的相似度的用户的id取出来(不要取它自己)
        for (UserSimilarityDTO userSimilarityDTO : minHeap) {
            similarityList.add(userSimilarityDTO.getUserId() == userId ? userSimilarityDTO.getUserRefId() : userSimilarityDTO.getUserId());
        }

        return similarityList;
    }

    /**
     * 到similarUserList中的用户访问的商家类目中查找userId不经常点击的商家类目中获得被推荐的类目id
     * @param userId 被推荐商品的用户id
     * @param similarUserList 用userId相似的用户集合
     * @param userActiveList 所有用户的浏览行为
     * @return 可以推荐给userId的商家类目id列表
     */
    public static List<Long> getRecommendateBusiness(Long userId, List<Long> similarUserList, List<UserActiveDTO> userActiveList) {
        List<Long> recommeddateBusinessList = new ArrayList<Long>();

        // userId的浏览行为列表
        List<UserActiveDTO> userIdActiveList = findUsersBrowsBehavior(userId, userActiveList);

        // 对userId的浏览行为按照商家类目id排个序，方便后续与推荐的用户的浏览行为中的商家类目的点击次数直接相减，避免时间复杂度为O(n2)
        Collections.sort(userIdActiveList, new Comparator<UserActiveDTO>(){
            @Override
            public int compare(UserActiveDTO o1, UserActiveDTO o2) {
                return o1.getBusinessId().compareTo(o2.getBusinessId());
            }
        });

        // 1.从与useId浏览行为相似的每个用户中找出一个推荐的商家类目
        for (Long refId : similarUserList) {
            // 计算当前用户所点击的商家类目次数与被推荐的用户所点击的商家类目的次数的差值
            // 找到当前这个用户的浏览行为
            List<UserActiveDTO> currActiveList = findUsersBrowsBehavior(refId, userActiveList);

            // 排序，同上述理由
            Collections.sort(currActiveList, new Comparator<UserActiveDTO>(){
                @Override
                public int compare(UserActiveDTO o1, UserActiveDTO o2) {
                    return o1.getBusinessId().compareTo(o2.getBusinessId());
                }
            });

            // 记录差值最大的商家类目的id
            long maxBusiness = 0L;

            // 记录最大的差值
            double maxDifference = 0.0;
            for (int i = 0; i < currActiveList.size(); i++) {
                // 求出点击量差值最大的商家类目，即为要推荐的类目
                double difference = Math.abs(currActiveList.get(i).getHits() - userIdActiveList.get(i).getHits());
                if (difference > maxDifference) {
                    maxDifference = difference;
                    maxBusiness = currActiveList.get(i).getBusinessId();
                }
            }
            recommeddateBusinessList.add(maxBusiness);
        }
        return recommeddateBusinessList;
    }

    /**
     * 找到当前用户的浏览行为列表
     * @param userId 当前用户id
     * @param userActiveList 所有用户的浏览行为列表
     * @return 当前用户的浏览行为列表
     */
    public static List<UserActiveDTO> findUsersBrowsBehavior(Long userId, List<UserActiveDTO> userActiveList) {
        List<UserActiveDTO> currActiveList = new ArrayList<UserActiveDTO>();
        for (UserActiveDTO userActiveDTO : userActiveList) {
            if (userActiveDTO.getUserId().equals(userId)) {
                currActiveList.add(userActiveDTO);
            }
        }
        return currActiveList;
    }

}