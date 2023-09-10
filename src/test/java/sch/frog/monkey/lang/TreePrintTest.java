package sch.frog.monkey.lang;

import sch.frog.monkey.lang.ast.IAstNode;
import sch.frog.monkey.lang.util.AstTreeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreePrintTest {

    /*
        sch.frog:FrogJson:jar:1.4
        +- org.openjfx:javafx-controls:jar:11.0.2:compile
        |  +- org.openjfx:javafx-controls:jar:win:11.0.2:compile
        |  \- org.openjfx:javafx-graphics:jar:11.0.2:compile
        |     +- org.openjfx:javafx-graphics:jar:win:11.0.2:compile
        |     |  +- aaa
        |     |  \- bbb
        |     \- org.openjfx:javafx-base:jar:11.0.2:compile
        |        \- org.openjfx:javafx-base:jar:win:11.0.2:compile
        +- org.openjfx:javafx-fxml:jar:11.0.2:compile
        |  \- org.openjfx:javafx-fxml:jar:win:11.0.2:compile
        |     +- ccc
        |     \- ddd
        +- org.fxmisc.richtext:richtextfx:jar:0.11.0:compile
        |  +- org.fxmisc.undo:undofx:jar:2.1.1:compile
        |  \- org.fxmisc.wellbehaved:wellbehavedfx:jar:0.3.3:compile
        +- org.fxmisc.flowless:flowless:jar:0.7.0:compile
        +- org.reactfx:reactfx:jar:2.0-M5:compile
        +- org.dom4j:dom4j:jar:2.1.4:compile
        \- org.yaml:snakeyaml:jar:2.0:compile
    */
    public static void main(String[] args){
        GeneralTree root = new GeneralTree("sch.frog:FrogJson:jar:1.4", new IAstNode[]{
                new GeneralTree("org.openjfx:javafx-controls:jar:11.0.2:compile", new IAstNode[]{
                        new GeneralTree("org.openjfx:javafx-controls:jar:win:11.0.2:compile"),
                        new GeneralTree("org.openjfx:javafx-graphics:jar:11.0.2:compile", new IAstNode[]{
                                new GeneralTree("org.openjfx:javafx-graphics:jar:win:11.0.2:compile", new IAstNode[]{
                                        new GeneralTree("aaa"),
                                        new GeneralTree("bbb")
                                }),
                                new GeneralTree("org.openjfx:javafx-base:jar:11.0.2:compile", new IAstNode[]{
                                        new GeneralTree("org.openjfx:javafx-base:jar:win:11.0.2:compile")
                                }),
                        }),
                }),
                new GeneralTree("org.openjfx:javafx-fxml:jar:11.0.2:compile", new IAstNode[]{
                        new GeneralTree("org.openjfx:javafx-fxml:jar:win:11.0.2:compile", new IAstNode[]{
                                new GeneralTree("ccc"),
                                new GeneralTree("ddd")
                        }),
                        new GeneralTree("eeeeeeeee", new IAstNode[]{
                                new GeneralTree("ffffff"),
                                new GeneralTree("ggggggg", new IAstNode[]{
                                        new GeneralTree("kkkkkkkkkk", new IAstNode[]{
                                                new GeneralTree("vvvvvvvvvvv", new IAstNode[]{
                                                        new GeneralTree("qwqwqwqwqwqwqwqw")
                                                })
                                        }),
                                        new GeneralTree("jdjdjdjdjd", new IAstNode[]{
                                                new GeneralTree("vlvlvlvlvlvlv")
                                        })
                                })
                        })
                }),
                new GeneralTree("org.fxmisc.richtext:richtextfx:jar:0.11.0:compile", new IAstNode[]{
                        new GeneralTree("org.fxmisc.undo:undofx:jar:2.1.1:compile"),
                        new GeneralTree("org.fxmisc.wellbehaved:wellbehavedfx:jar:0.3.3:compile")
                }),
                new GeneralTree("org.fxmisc.flowless:flowless:jar:0.7.0:compile"),
                new GeneralTree("org.reactfx:reactfx:jar:2.0-M5:compile"),
                new GeneralTree("org.dom4j:dom4j:jar:2.1.4:compile"),
                new GeneralTree("org.yaml:snakeyaml:jar:2.0:compile"),
        });
        String content = AstTreeUtil.generateTree(root);
        System.out.println(content);
    }

    public static class GeneralTree implements IAstNode {

        private final ArrayList<IAstNode> children = new ArrayList<>();

        private final String literal;

        public GeneralTree(String literal) {
            this.literal = literal;
        }

        public GeneralTree(String literal, IAstNode[] nodes){
            this.literal = literal;
            children.addAll(Arrays.asList(nodes));
        }

        @Override
        public List<IAstNode> getChildren() {
            return children;
        }

        @Override
        public String toString() {
            return this.literal;
        }
    }

}
