package services;

import java.util.List;

import beans.Response;
import beans.Tips;
import beans.Topic;

public interface DiscussisonDao {
 public int addDiscussion(Topic topic);
 public int getResponseTotal(int tid);
 public int getTopicTotal(int sid,int ceid,int mid);
 public List<Topic> getTopiclist(int sid,int ceid,int mid,int rp,int page);
 public List<Topic>getMyTopics(String email,int page,int rp);
 public int doresponse(Response response,int prid);
 public List<Response>getResponselist(int tid,int page,int rp);
 public int getMyTopicTotal(String email);
 
 public void saveTopPic(int tid,String path);
 public void saveResponsePic(int tid,String path);
 public String getResponsePic(int tid);
 //public List<Tips> getTips(int kind,int sid,int ceid,int mid,int page,int rp);
 //public int tipCount(int kind,int sid,int ceid,int mid,int page,int rp);
}
