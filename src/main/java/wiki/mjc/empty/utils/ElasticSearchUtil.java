package wiki.mjc.empty.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.stereotype.Service;

@Service("elasticSearchUtil")
public class ElasticSearchUtil {

	protected Logger logger = LogManager.getLogger("test");
	
	private TransportClient client = null;
	
	public ElasticSearchUtil() {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch")
	            .put("xpack.security.user", "elastic:changeme").build();
		try {
			client = new PreBuiltXPackTransportClient(settings)
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		} catch (UnknownHostException e) {
			logger.error("init ElasticSearch client failed");
		}
	}
	
	/** Document Api Start *************/
	public int index(String index, String type,String source) {
		return index(index, type, null, source);
	}
	
	public int index(String index, String type, String id, String source) {
		IndexResponse response = client.prepareIndex(index, type, id).setSource(source).get();
		return (response==null || response.getResult() == null )? -1: response.getResult().getOp();
	}
	
	public String get(String index, String type, String id) {
		GetResponse response = client.prepareGet(index, type, id).get();
		return response.getSourceAsString();	
	}
	
	public int delete(String index, String type, String id) {
		DeleteResponse response = client.prepareDelete(index, type, id).get();
		return (response==null || response.getResult() == null )? -1: response.getResult().getOp();
	}
	
	/*
	 * just one example
	 * see more in page
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/5.0/java-docs-update.html
	 */
	public int update(String index, String type, String id, String source) {
		UpdateResponse response = client.prepareUpdate(index, type, id).setDoc(source).get();
		return (response==null || response.getResult() == null )? -1: response.getResult().getOp();
	}
	
	/*
	 * just one example
	 * see more in page
	 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/5.0/java-docs-multi-get.html
	 */
	public Map<String, String> multiGet(String index, String type, List<String> ids) {
		Map<String, String> map = new HashMap<String, String>();
		MultiGetResponse multiGetItemResponses = client.prepareMultiGet().add(index, type, ids).get();
		for (MultiGetItemResponse itemResponse : multiGetItemResponses) { 
		    GetResponse response = itemResponse.getResponse();
		    if (response.isExists()) {
		    	String id =response.getId();
		        String json = response.getSourceAsString();
		        map.put(id, json);
		    }
		}
		return map;
	}
	/** Document Api End *************/
	
	/** Search Api Start *************/
	public SearchHits search(String index, String type, String id) {
		
		//SearchResponse response = client.prepareSearch().get();
		
		
		SearchResponse response = client.prepareSearch("index1", "index2")
		        .setTypes("type1", "type2")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.termQuery("multi", "test"))                 // Query
		        .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
		        .setFrom(0).setSize(60).setExplain(true)
		        .get();	
		
		//QueryBuilder qb = matchAllQuery();
		
		return response.getHits();
	}
	/** Search Api End *************/
	
	
    public static void main(String[] args)  {
    	
    }

	
}
