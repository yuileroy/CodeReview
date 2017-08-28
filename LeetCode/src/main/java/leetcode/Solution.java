package leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*

String[][] flights = {
  { "Paris", "London", "150" },
  { "Paris", "Istanbul", "1100" },
  { "Paris", "Dubai", "400" },
  { "London", "Tokyo", "1000" },
  { "Istanbul", "Dubai", "200" },
  { "Dubai", "Tokyo", "200" },
  { "Beijing", "Shanghai", "50" },
  {  "Shanghai", "Beijing", "55" },
  { "London", "Beijing", "300" },
  { "Beijing", "Tokyo", "120" },
  { "Shanghai", "Tokyo", "65" },
};
paris -> tokyo
return: [paris, dubai, tokyo], 400 + 200

 */
/*
class Solution {

	class CityPrice {
		String city;
		int price;

		CityPrice(String city, int price) {
			this.city = city;
			this.price = price;
		}
	}

	class Result {
		List<String> cities = new ArrayList<>();
		int totalPrice = 0;

		Result() {
		}
	}

	// a -> b, a -> c, b -> c,

	Result getFlight(String[][] flights, String fromCity, String toCity, int maxNum){
	    Map<String, List<CityPrice>> map = new HashMap<>();
	    for (String[] flight : flights) {
	      if (!map.containsKey(flight[0])) {
	        map.put(flight[0], new ArrayList<CityPrice>());
	      }
	      map.get(flight[0]).add(new CityPrice(flight[1], Integer.parseInt(flight[2])));
	    }
	    
	    Result start = new Result();
	    start.cities.add(fromCity);
	    
	    helper(start, map, toCity, 0, maxNum);
	}

	void helper(Result result,  Map<String, List<CityPrice>> map, String toCity, int curr, int maxNum) {
	      
	//      if (result.ci .equals //String lastCity = liPre.get(liPre.size() -1 );) {
	//        result.price ==
	//      }

	List<String> liPre = result.cities;
	String lastCity = liPre.get(liPre.size() - 1);
	List<CityPrice> li = map.get(lastCity);
	Result copy = new Result(result); // copy

	for(
	CityPrice cp:li)
	{
		copy.cities.add(cp.city);
		copy.price += cp.price;
	}

	helper(copy, map, toCity, curr+1, maxNum);
	    }}
}
*/