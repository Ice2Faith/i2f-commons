package i2f.commons.core.utils.jdbc.wrapper.base;

public class SqlBase {
    public static enum JoinType{
        INNER("inner"),
        LEFT("left"),
        RIGHT("right"),
        OUTER("outer");
        private String type;
        private JoinType(String type){
            this.type=type;
        }
        public String type(){
            return this.type;
        }

    }
    public static enum LinkType{
        AND("and"),
        OR("or");
        private String linker;
        private LinkType(String linker){
            this.linker=linker;
        }
        public String linker(){
            return this.linker;
        }

    }
    public static enum SortType{
        ASC("asc"),
        DESC("desc");
        private String sort;
        private SortType(String sort){
            this.sort=sort;
        }
        public String sort(){
            return this.sort;
        }

    }
}
