public class TestMain{

/**
   * Split a list into consecutive days by date ranges. 
   * please refer to ColUtilTest. 
   * 
   * for example: day1: obj1, obj2, obj3
   *              day2: obj4,
   *              day3: obj5,obj6
   * The objects are split into three groups:
   * day1-day3:obj1,obj4,obj5
   * day1:obj2,obj3
   * day3:obj6
   * 
   * another example:
   * day1:obj1
   * day2:obj2,obj3,obj4
   * day3:obj5,obj6
   * day4:obj7,obj8
   * The objects are split into three groups:
   * day1-day4:obj1,obj2,obj5,obj7
   * day2-day4:obj3,obj6,obj8
   * day2:obj4
   * 
   * @param lst
   * @param dateFunc
   * @return
   */
  
  public static <T> Map<CalendarRange, List<T>> splitListIntoConsecutiveDateByGroup( List<List<T>> lst, Function<T, Calendar> dateFunc ) {

    Map<CalendarRange, List<T>> ret = new HashMap<>();
    for ( List<T> eachRowItem : lst ) {
      
      List<List<T>> splitIntoConsecutiveDay = ColUtil.groupByListByConsecutiveDates( eachRowItem, dateFunc );
      for ( List<T> eachItem : splitIntoConsecutiveDay ) {
        CalendarRange dateRange = ColUtil.getCalendarRangeForList( eachItem, dateFunc );

        List<T> oneItem = ret.get( dateRange );
        if ( oneItem == null ) {
          oneItem = new ArrayList<>();
          ret.put( dateRange, oneItem );
        }
        oneItem.addAll( eachItem );
      }
    }
    return ret;
  }
  
  public static <T> CalendarRange getCalendarRangeForList( List<T> lst, Function<T,Calendar> dataFunc ) {
    Calendar startDate = null;
    Calendar endDate = null;
    for(T ite:lst){
      if ( startDate == null || startDate.after( dataFunc.apply(ite) ) ) {
        startDate = (Calendar)  (dataFunc.apply(ite)).clone();
      }
      if ( endDate == null || endDate.before( (dataFunc.apply(ite)) ) ) {
        endDate = (Calendar)(dataFunc.apply(ite)).clone();
      }
    }
    
    endDate.add( Calendar.DATE, 1 );
    return new CalendarRange( startDate, endDate );
  }
  
  public static <T> List<List<T>> groupByListByConsecutiveDates(List<T> lst, Function<T,Calendar> dateFunc){
    List<List<T>> ret = new ArrayList<>();  
    Collections.sort(lst, new Comparator<T>(){
      public int compare(T obj1, T obj2){
        return dateFunc.apply(obj1).before(dateFunc.apply(obj2))?-1:1;
      }
    }
    );
    
    int index = 0;
    boolean stop = false;
    while(!stop){
      List<T> feeGroup = new ArrayList<T>();
      for ( int i = index; i < lst.size(); i++) {
        if(i == lst.size() - 1){
          stop = true;
        }
        T obj1 = lst.get(i);
        if(i==index){
          feeGroup.add(obj1);
        }else{
          T obj2 = lst.get(i-1);
          if( DateUtil.getDifferenceDay( dateFunc.apply(obj1),  dateFunc.apply(obj2))<=1){
            feeGroup.add(obj1);
          }else{
            index = i;
            stop=false;
            break;
          }
        }
        
      }
      ret.add(feeGroup);
    }    
    return ret;
  }


  
}
