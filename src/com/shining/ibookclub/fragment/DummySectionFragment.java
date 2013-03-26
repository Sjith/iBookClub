package com.shining.ibookclub.fragment;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;




import org.apache.http.NameValuePair;

import org.apache.http.message.BasicNameValuePair;

import org.json.JSONObject;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shining.ibookclub.*;
import com.shining.ibookclub.bean.BookBean;
import com.shining.ibookclub.dao.BookInfoDao;
import com.shining.ibookclub.dao.MyBookDao;
import com.shining.ibookclub.support.FinalConstants;
import com.shining.ibookclub.support.HttpUtility;
import com.shining.ibookclub.support.ImageLoaderTask;
import com.shining.ibookclub.support.KeywordsFlow;
import com.shining.ibookclub.support.LazyAdapter;
import com.shining.ibookclub.support.LazyScrollView;
import com.shining.ibookclub.support.LazyScrollView.OnScrollListener;
import com.shining.ibookclub.support.LoginSingleton;
import com.shining.ibookclub.support.PullToRefreshListView;
import com.shining.ibookclub.support.PullToRefreshListView.OnRefreshListener;
import com.shining.ibookclub.support.TaskParam;


import android.content.Intent;
import android.content.res.AssetManager;

import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.webkit.WebView;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public  class DummySectionFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	
//	public static final Object lock = new Object();  
	
	private int SEC_NUMBER_INTEGER;
	

	
	private WebView webview_BookForBorrow;
	
	
	private Handler handler = new Handler();
	
	
	private TextView text_nickname;
	
	private SearchView searchView;
	
	private KeywordsFlow keywordsFlow; 
	
	private PullToRefreshListView pullToRefreshView;
	
	

	
	private Button button_post;
	
	private Button button_myborrow;
	
	private Button button_buyBook;
	
	private Button button_findNearbyBook;
	
	private ImageButton button_tweet;
	
	private String keyword; 
	
	 public static final String[] keywords = { 
		 "Python参考手册","谣言粉碎机","Java并发编程实战",
		 "物联网安全","打造Facebook","知日·奈良美智","白夜行",
		 "白帽子讲web安全","知日·书之国","深入Android应用开发","青春",
		 "MySQL高效编程","浪潮之巅","乔布斯传","研磨Struts2",
		 "冰与火之歌","追风筝的人","商业的常识","活着",
		 "不能承受的生命之轻","云图","1Q84","动物农场"
	 };  
	
	  private ArrayList<BookBean> mListItems;
	
	private static String nickname;
	
	
	
	
	ArrayList<BookBean> bookList=new ArrayList<BookBean>();
	

	
	
	private LazyScrollView waterfall_scroll;
	private LinearLayout waterfall_container;
	private ArrayList<LinearLayout> waterfall_items;
	private Display display;
	private AssetManager assetManager;
	private List<String> image_filenames;
	private final String image_path = "images";

	private int itemWidth;

	private int column_count = 3;// 显示列数
	private int page_count = 15;// 每次加载15张图片

	private int current_page = 0;
	
	private LazyAdapter  adapter;
	

	public DummySectionFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
		
		
		
		SEC_NUMBER_INTEGER=getArguments().getInt(ARG_SECTION_NUMBER);
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		textView.setText(Integer.toString(getArguments().getInt(
				ARG_SECTION_NUMBER)));
		textView.setTypeface(iBookClub.typeFace);
	
		if(SEC_NUMBER_INTEGER==1){
			
			
			return inflater.inflate(R.layout.fragment_borrow, container, false);
		}
		else if(SEC_NUMBER_INTEGER==2){
			return inflater.inflate(R.layout.fragment_timeline, container,false);
		}
		else if(SEC_NUMBER_INTEGER==3){
			return inflater.inflate(R.layout.fragment_discover, container,false);
		}
		
		return textView;
	}
	
	 public void onActivityCreated(Bundle savedInstanceState){
			
		    super.onCreate(savedInstanceState);
		   
		    if(SEC_NUMBER_INTEGER==1){
		    	
		    	
		    //	GetPublicBookInfo getPublicBook=new GetPublicBookInfo();
		    //	getPublicBook.execute((Void)null);
		    	
		    	searchView=(SearchView)getActivity().findViewById(R.id.searchView);
		    	
		    	searchView.setOnQueryTextListener(new OnQueryTextListener(){

					@Override
					public boolean onQueryTextChange(String arg0) {
						
						return false;
					}

					@Override
					public boolean onQueryTextSubmit(String arg0) {
						
						keyword=searchView.getQuery().toString();
						
						SearchPublicBookTask search=new SearchPublicBookTask();
						search.execute((Void) null);
						
						return true;
					}
		    	});
		    	
		      	
		    	
		    	
		    	  keywordsFlow = (KeywordsFlow)getActivity().findViewById(R.id.keyWordsFlow);  
		    	  
		    	  keywordsFlow.setOnItemClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						
						
						keyword=((TextView)v).getText().toString();
						SearchPublicBookTask search=new SearchPublicBookTask();
						search.execute((Void) null);
						
					}
		    	  });
		    	  
		    	
		    	  
		    	  keywordsFlow.setDuration(800l);  
		    
		    	   keywordsFlow.rubKeywords(); 
	    		   feedKeywordsFlow(keywordsFlow, keywords);  
		    	  
		    	   if(Math.random()>0.5){
		    		   keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
		    	   }
		    	   else{
		    		   keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT); 
		    	   }
		    	
		    	
		    	webview_BookForBorrow=(WebView)getActivity().findViewById(R.id.webview_BookForBorrow);
		    	
		    	webview_BookForBorrow.setVisibility(View.GONE);
		    	
		    	
		    	button_buyBook=(Button)getActivity().findViewById(R.id.button_buybook);
		    	
		    	button_buyBook.setOnClickListener(new OnClickListener(){

					
					public void onClick(View v) {
						
						Intent intent=new Intent(getActivity(),BuyBookActivity.class);
						intent.putExtra("keyword", keyword);
						startActivity(intent);
						
					}
		    	});
		    	
		    	button_buyBook.setTypeface(iBookClub.typeFace);
		    	
		    	
		    	
		
		    }
		    else if(SEC_NUMBER_INTEGER==2){
		    	
		    
		    	button_post=(Button)getActivity().findViewById(R.id.button_post);
		    	
		    	button_post.setOnClickListener(new OnClickListener(){

					
					public void onClick(View v) {
						Intent intent=new Intent(getActivity(),PostActivity.class);
						startActivity(intent);
						
					}
		    		
		    	});
		    	
		    	button_myborrow=(Button)getActivity().findViewById(R.id.button_myborrow);
		    	
		    	button_myborrow.setOnClickListener(new OnClickListener(){

					
					public void onClick(View v) {
						Intent intent=new Intent(getActivity(),BorrowActivity.class);
						startActivity(intent);
						
					}
		    		
		    	});
		    	
		    	button_tweet=(ImageButton)getActivity().findViewById(R.id.button_tweet);
		    	
		    	button_tweet.setOnClickListener(new OnClickListener(){
		    		
		    		public void onClick(View v){
		    			
		    			Intent intent=new Intent(getActivity(),PostTweetActivity.class);
						startActivity(intent);
		    		}
		    	});
		    	
		    	pullToRefreshView=(PullToRefreshListView)getActivity().findViewById(R.id.pullToRefresh);
		    	
		    	pullToRefreshView.setOnRefreshListener(new OnRefreshListener() {
		            @Override
		            public void onRefresh() {
		                // Do work to refresh the list here.
		                new GetDataTask().execute();
		            }
		        });
		    	
		    	pullToRefreshView.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						String isbn=bookList.get(position-1).getIsbn();
						
						if(isbn!=null){
				
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									BookDetailActivity.class);
							
							intent.putExtra("ISBN", isbn);
							startActivity(intent);
						}
						
						
					}
		    	});
		    	
		    	pullToRefreshView.onRefresh();

		        mListItems = new ArrayList<BookBean>();
		        mListItems.addAll(bookList);
		        
		        adapter=new LazyAdapter(getActivity(), bookList);

		        pullToRefreshView.setAdapter(adapter);

		    	
		    }
		    else if(SEC_NUMBER_INTEGER==3){
		    	
		    	
		    	
		    	text_nickname=(TextView)getActivity().findViewById(R.id.text_nickname);
		    	text_nickname.setTypeface(iBookClub.typeFace);
		    	
		    	Bundle bundle=getActivity().getIntent().getExtras();
		    	if(bundle!=null){
		    		nickname=bundle.getString("nickname");
		    		text_nickname.setText(nickname);
		    	}
		    	

				button_findNearbyBook=(Button)getActivity().findViewById(R.id.button_findNearbyBook);
		    	
		    	button_findNearbyBook.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
					
						Intent intent=new Intent(getActivity(),FindNearbyBookActivity.class);
						startActivity(intent);
					}
		    	});
		    	
		    	display = getActivity().getWindowManager().getDefaultDisplay();
				itemWidth = display.getWidth() / column_count;// 根据屏幕大小计算每列大小
				assetManager = getActivity().getAssets();

				InitLayout();
				
		    	
		    	
		    	
		    	
		    }
		   
	     
	 
	}
	 
	 private void InitLayout() {
			waterfall_scroll = (LazyScrollView) getActivity().findViewById(R.id.waterfall_scroll);
			waterfall_scroll.getView();
			waterfall_scroll.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onTop() {
					// 滚动到最顶端
					Log.d("LazyScroll", "Scroll to top");
				}

				@Override
				public void onScroll() {
					// 滚动中
					Log.d("LazyScroll", "Scroll");
				}

				@Override
				public void onBottom() {
					// 滚动到最低端
					AddItemToContainer(++current_page, page_count);
				}
			});

			waterfall_container = (LinearLayout) getActivity()
					.findViewById(R.id.waterfall_container);
			waterfall_items = new ArrayList<LinearLayout>();

			for (int i = 0; i < column_count; i++) {
				LinearLayout itemLayout = new LinearLayout(getActivity());
				LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
						itemWidth, LayoutParams.WRAP_CONTENT);
				// itemParam.width = itemWidth;
				// itemParam.height = LayoutParams.WRAP_CONTENT;
				itemLayout.setPadding(2, 2, 2, 2);
				itemLayout.setOrientation(LinearLayout.VERTICAL);

				itemLayout.setLayoutParams(itemParam);
				waterfall_items.add(itemLayout);
				waterfall_container.addView(itemLayout);
			}

			// 加载所有图片路径

			try {
				image_filenames = Arrays.asList(assetManager.list(image_path));

			} catch (IOException e) {
				e.printStackTrace();
			}
			// 第一次加载
			AddItemToContainer(current_page, page_count);
		}

		private void AddItemToContainer(int pageindex, int pagecount) {
			int j = 0;
			int imagecount = image_filenames.size();
			for (int i = pageindex * pagecount; i < pagecount * (pageindex + 1)
					&& i < imagecount; i++) {
				j = j >= column_count ? j = 0 : j;
				AddImage(image_filenames.get(i), j++);

			}

		}

		private void AddImage(String filename, int columnIndex) {
			ImageView item = (ImageView) LayoutInflater.from(getActivity()).inflate(
					R.layout.waterfallitem, null);
			waterfall_items.get(columnIndex).addView(item);

			TaskParam param = new TaskParam();
			param.setAssetManager(assetManager);
			param.setFilename(image_path + "/" + filename);
			param.setItemWidth(itemWidth);
			ImageLoaderTask task = new ImageLoaderTask(item);
			task.execute(param);

		}
	 
	 private class GetDataTask extends AsyncTask<Void, Void, ArrayList<BookBean>> {

	        @Override
	        protected ArrayList<BookBean> doInBackground(Void... arg0) {
	           
	        	
	        	String httpUrl=FinalConstants.SERVER_URL+"getRecentBook.action";
				
				if(LoginSingleton.isLoginSuccess()){
					
				
					List <NameValuePair> params = new ArrayList <NameValuePair>(); 
			        params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail));  
			
					try{
				
						HttpUtility httpUtility=new HttpUtility(httpUrl,params);
						
						String strResult=httpUtility.doPost();
						System.out.println("GetRecentBook:"+strResult);
						Gson gson = new Gson();
						bookList = gson.fromJson(strResult, new TypeToken<ArrayList<BookBean>>(){}.getType());
						
					}
					catch(Exception e){
						e.printStackTrace();
					}
		
				}
				
	            return bookList;
	        }

	        @Override
	        protected void onPostExecute(ArrayList<BookBean> result) {
	        //    mListItems.addFirst("Added after refresh...");
	        	
	        	//TODO 个人状态部分未完全实现，以下仅为静态展示用
	        	
	        	String str[]={"人生也许就是不断地放下，然而令人痛心的是，我都没能好好地与他们道别",
	        				"陌上花开，慢慢醉~",
	        				"清明快到了，出游计划走起来吧~",
	        				"我看到的柴静对未知充满了好奇和敬畏。她说记者就是在攫取，在剥离一个个灵魂。她总爱刨根问底，程度之深到了她自己都知道或多或少有贬义。 ",
	        				"世界上不止一个人落在哈佛拥有才华横溢的校友，也不止一个人可以接触到这些顶级风投精英，但只有一个马克·扎克伯格。",
	        				"曾经读过很多书，或许他们的文风语言够华丽，但是他们都是取悦或者虐待于人的精神，从来没有像平凡的世界这样，贯入人以强劲的价值观！",
	        				"其实在青年的道路中，我们的大多数我想都属于这样，为了自己的生活为了自己的生活意义在世界上忙碌的奔波着。",
	        				"我始终认为，一本书好的标准是看能不能深刻影响人，深刻影响他们走向未来的方向。这才是评价书的标准！",
	        	};
	        	
	        	for(int i=0;i<str.length;i++){
	        		BookBean bean=new BookBean();
	        		bean.setBookname(str[i]);
	        		
	        		result.add((int)(Math.random()*15), bean);
	        	}
	        	
	        	//TODO
	        	
	            adapter=new LazyAdapter(getActivity(), result);
	            
	            pullToRefreshView.setAdapter(adapter);

	            // Call onRefreshComplete when the list has been refreshed.
	            pullToRefreshView.onRefreshComplete();

	            super.onPostExecute(result);
	        }
	    }

	 private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {  
		    Random random = new Random();  
		    for (int i = 0; i < KeywordsFlow.MAX; i++) {  
		        int ran = random.nextInt(arr.length);  
		        String tmp = arr[ran];  
		        keywordsFlow.feedKeyword(tmp);  
		    }  
		}  

	 

	public class SearchPublicBookTask extends AsyncTask<Void,Void,Boolean>{
	 
	 protected Boolean doInBackground(Void... arg0){
		 
		 Boolean result=false;
		 String httpUrl=FinalConstants.SERVER_URL+"searchBook.action";
		 
		 if(LoginSingleton.isLoginSuccess()){
		 
			 List <NameValuePair> params = new ArrayList <NameValuePair>(); 
			 params.add(new BasicNameValuePair("email", LoginSingleton.loginEmail)); 
			 params.add(new BasicNameValuePair("keyword",keyword));
			
			 try{
				
				HttpUtility httpUtility=new HttpUtility(httpUrl,params);
				String strResult=httpUtility.doPost();
						
				System.out.println("SearchPublicBook:"+strResult);
				Gson gson = new Gson();
				bookList = gson.fromJson(strResult, new TypeToken<ArrayList<BookBean>>(){}.getType());
				result=true;
			 }
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return result;
	}
		 
	 
	 
	 protected void onPostExecute(final Boolean success){
		 
		 button_buyBook.setVisibility(View.VISIBLE);
		 
		 if(bookList.isEmpty()){
			 Toast.makeText(getActivity(), "图书馆中暂时没有您需要的书！", Toast.LENGTH_SHORT).show();
		 }
		 else if(success){
			 
			 BookInfoDao.getInstance().deleteAll();
		
			 for(BookBean book:bookList){
						
				BookInfoDao.getInstance().create(book);
			 }
				
			 LoadSearchResult();
			    
			 webview_BookForBorrow.reload();
		 }
	}
}
	private void LoadSearchResult(){
		
		webview_BookForBorrow.setVisibility(View.VISIBLE);
		
		webview_BookForBorrow.getSettings().setSupportZoom(false);
    	webview_BookForBorrow.getSettings().setJavaScriptCanOpenWindowsAutomatically(
				true);
    	webview_BookForBorrow.getSettings().setJavaScriptEnabled(true);

    	webview_BookForBorrow.loadUrl("file:///android_asset/book_list_borrow.html");
    	
    	webview_BookForBorrow.addJavascriptInterface(new Object() {
			public String getBookResult() {
				return BookInfoDao.getInstance().list().toString();
			}

	
		

			public void getDetail(final String isbn) {
				
				handler.post(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent();
						intent.setClass(getActivity(),
								BookDetailActivity.class);
						intent.putExtra("ISBN", isbn);
						startActivity(intent);
					}
				});
			}
			
		

	
		}, "bookShelfControl");
    	
    	getActivity().findViewById(R.id.keyWordsFlow).setVisibility(View.GONE);
    	
	}
	 
	
		
	
	
	 
	 public void onResume() {  
		  
	        super.onResume();  
	        if(SEC_NUMBER_INTEGER==1){
	        	webview_BookForBorrow.reload();
	        }else if(SEC_NUMBER_INTEGER==2){
	        	
	        }else if(SEC_NUMBER_INTEGER==3){
	        	
	        	Bundle bundle=getActivity().getIntent().getExtras();
		    	if(bundle!=null){
		    		nickname=bundle.getString("nickname");
		    		text_nickname.setText(nickname);
		    	}
	        }
	    }  
	 

		
	 
}
