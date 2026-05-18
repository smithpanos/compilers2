import syntaxtree.*;
import visitor.*;



class MyVisitor extends GJDepthFirst<String, String>{

    private static final boolean DEBUG = false;

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    @Override
    public String visit(MainClass n, String argu) throws Exception {
        if(DEBUG) {System.out.println("Start visitor."); }
        String classname = n.f1.accept(this, null);
        System.out.println("Main Class: " + classname);

        super.visit(n, argu);

        System.out.println();

        return null;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    @Override
    public String visit(ClassDeclaration n, String argu) throws Exception {
        if(DEBUG) {System.out.println("Starting a Class");}
        n.f0.accept(this, argu);

        String classname = n.f1.accept(this, argu);
        System.out.println("Class: " + classname);

        n.f2.accept(this, argu);
        // System.out.println("Fields: ");
        n.f3.accept(this, "FIELD");
        // System.out.println("Methods: ");
        n.f4.accept(this, "METHOD");
        n.f5.accept(this, argu);

        System.out.println();

        return null;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    @Override
    public String visit(ClassExtendsDeclaration n, String argu) throws Exception {
        if(DEBUG) {System.out.println("Starting an extended class.");}
        n.f0.accept(this, argu);

        String classname = n.f1.accept(this, null);
        

        n.f2.accept(this, argu);
        String temp = n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        System.out.println("Class:" + classname + " Extents: " + temp);
        // System.out.println("Fields: ");
        n.f5.accept(this, "FIELD");
        // System.out.println("Methods: ");
        n.f6.accept(this, "METHOD");
        n.f7.accept(this, argu);

        System.out.println();

        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public String visit(VarDeclaration n, String argu) throws Exception {
        if(DEBUG) {System.out.println("Started a VarDeclaration");}
        String _ret=null;
        String type = n.f0.accept(this, argu);
        String var = n.f1.accept(this, argu);
        System.out.println(argu + " " + var + " " + type);
        super.visit(n, argu);
        
        return _ret;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    @Override
    public String visit(MethodDeclaration n, String argu) throws Exception {
        if(DEBUG) {System.out.println("Started a method declaration.");}

        String myType = n.f1.accept(this, null);
        String myName = n.f2.accept(this, null);

        System.out.println(argu + " " + myName + " returns " +  myType );
        // System.out.println("Local vars:");

        String temp = n.f4.present() ? n.f4.accept(this, "PARAM") : "";

        n.f7.accept(this, "LOCAL");


        // super.visit(n, null);
        return null;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> FormalParameterTail()
     */
    @Override
    public String visit(FormalParameterList n, String argu) throws Exception {
        if(DEBUG) {System.out.println("Started a formal parameters list");}
        String ret = n.f0.accept(this, argu);
        System.out.println( argu + " " + ret );
        String ret1 = null;
        if (n.f1 != null) {
            ret1 = n.f1.accept(this, argu);
            System.out.println( argu + " " + ret1 );
        }
        if (ret1 != null) {ret += ret1;}
        return ret;
    }

    /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
    public String visit(FormalParameterTerm n, String argu) throws Exception {
        if (DEBUG) {System.out.println("Started a formal parameter term");}
        // System.out.println("FORMA PARAMETER: " + argu + " " + n.f1);
        return n.f1.accept(this, argu);
    }

    /**
    * f0 -> ( FormalParameterTerm() )*
    */
    @Override
    public String visit(FormalParameterTail n, String argu) throws Exception {
        if(DEBUG) {System.out.println("Started a formal parameter tail");}
        String ret = "";
        for ( Node node: n.f0.nodes) {
            ret += ", " + node.accept(this, null);
            // System.out.println("HEYYYYYYYY local parameters term: " + argu + " " + ret);

        }

        return ret;
    }

    /**
     * parameters of the functions goes before class declaration
     * f0 -> Type()
     * f1 -> Identifier()
     */
    @Override
    public String visit(FormalParameter n, String argu) throws Exception{
        if(DEBUG) {System.out.println("Started a formal parameter");}
        String type = n.f0.accept(this, null);
        String name = n.f1.accept(this, null);
        // System.out.println("HEYYYYYYYY formal parameters: " + argu + " type: " + type + ", name: " + name);

        return type + " " + name;
    }

    @Override
    public String visit(ArrayType n, String argu) {
        return "int[]";
    }

    @Override
    public String visit(BooleanType n, String argu) {
        return "boolean";
    }

    @Override
    public String visit(IntegerType n, String argu) {
        return "int";
    }

    /**
    * f0 -> <IDENTIFIER>
    */
    @Override
    public String visit(Identifier n, String argu) {
        // System.out.println("IDENTIFIER: " + argu + " " + n.f0);
        return n.f0.toString();
    }
}
