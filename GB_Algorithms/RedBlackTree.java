package GB_Algorithms;

/**
 * Класс RedBlackTree, отвечающий за структуру данных "красно-черное дерево"
 * Данный класс включает в себя методы добавление элементов в структуру и балансировки
 * В том числе методы для левого и правого поворота, а также смены цвета
 */

public class RedBlackTree {

    /** Поле корень дерева */
    private Node root;

    /**
     * Метод для случая добавления элемента в корень дерева
     * @param value - значение для добавления в корень
     * @return возращает булевое значение
     */
    public boolean add(int value) {
        // рассматриваем случай, когда корень уже есть
        if (root != null) {
            boolean result = addNode(root, value);
            root = rebalance(root);
            root.color = Color.BLACK;
            return result;
        // случай, когда дерево пустое
        } else {
            root = new Node();
            root.color = Color.BLACK;
            root.value = value;
            return true;
        }
    }

    /**
     * Метод балансировки красно-черного дерева
     * @param node - принимает текущую ноду
     * @return - возвращает текущую ноду по результатам балансировки
     */
    private Node rebalance(Node node) {
        Node result = node;
        boolean needRebalance;
        // выполняется, пока балансировка необходима
        do {
            needRebalance = false;
            /*
            * случай, когда есть красный правый ребенок, а левого ребенка либо нет, либо он черный 
            * (по правилам левостороннего дерева, необходимо выполнить правый поворот)
             */ 
            if (result.rightChild != null && result.rightChild.color == Color.RED &&
                    (result.leftChild == null || result.leftChild.color == Color.BLACK)) {
                        needRebalance = true;
                        result = rightSwap(result);
            }
            /*
             * случай, когда есть красный левый ребенок и его левый ребенок тоже красный 
             * (по правилам данной структуры, две красных ноды подряд быть не должно, поэтому выполняем левый поворот)
             */
            if (result.leftChild != null && result.leftChild.color == Color.RED &&
                    result.leftChild.leftChild != null && result.leftChild.leftChild.color == Color.RED) {
                        needRebalance = true;
                        result = leftSwap(result);
            }
            /*
             * случай, когда и левый, и правый ребенок являются красными, для балансировки необходимо поменять цвета
             */
            if (result.leftChild != null && result.leftChild.color == Color.RED &&
                    result.rightChild != null && result.rightChild.color == Color.RED) {
                        needRebalance = true;
                        colorSwap(result);
            }
        }
        while (needRebalance);
        return result;
    }

    /**
     * Метод для добавления элемента в красно-черное дерево
     * @param node - принимает текущую ноду
     * @param value - принимает значение для вставки
     * @return - возвращает булевое значение
     */
    private boolean addNode(Node node, int value) {
        /*
         * Данный метод является рекурсивным, поэтому в случае, когда необходимое значение уже добавлено,
         * необходимо вернуть false и завершить рекурсию
         */
        if (node.value == value) { 
            return false;
        } else {
            // Случай, когда текущее значение больше (означает, что добавляемый элемент станет левым ребенком)
            if (node.value > value) {
                // Если левый ребенок уже есть, запускается рекурсия и выполняется до тех пор, пока не найдется нужное место
                if (node.leftChild != null) {
                    boolean result = addNode(node.leftChild, value);
                    node.leftChild = rebalance(node.leftChild);
                    return result;
                } else {
                    // Если левого ребенка нет, то вставляется принятый методом элемент, цвет по умолчанию - красный
                    node.leftChild = new Node();
                    node.leftChild.color = Color.RED;
                    node.leftChild.value = value;
                    return true;
                }
                // Иной случай, когда вставляемое значение не больше чем текущее
            } else {
                // Выполняются аналогичные проверки для правого ребенка
                if (node.rightChild != null) {
                    boolean result = addNode(node.rightChild, value);
                    node.rightChild = rebalance(node.rightChild);
                    return result;
                } else {
                    node.rightChild = new Node();
                    node.rightChild.color = Color.RED;
                    node.rightChild.value = value;
                    return true;
                }
            }
        }
    }

    /**
     * Метод правого поворота
     * @param node - принимает текущую ноду
     * @return - возвращает правого ребенка текущей ноды
     */
    private Node rightSwap(Node node) {
        Node rightChild = node.rightChild;
        // Выбирается промежуточный элемент, относительно которого будет совершаться поворот
        Node betweenChild = rightChild.leftChild;
        rightChild.leftChild = node;
        node.rightChild = betweenChild;
        rightChild.color = node.color;
        node.color = Color.RED;
        return rightChild;
    }

    /**
     * Метод для левого поворота
     * @param node - принимает текущую ноду
     * @return - возвращается левого ребенка текущей ноды
     */
    private Node leftSwap(Node node) {
        Node leftChild = node.leftChild;
        // Аналогично правому повороту, выбирается промежуточный элемент, относительно которого будет совершаться поворот
        Node betweenChild = leftChild.rightChild;
        leftChild.rightChild = node;
        node.leftChild = betweenChild;
        leftChild.color = node.color;
        node.color = Color.RED;
        return leftChild;
    }

    /**
     * Метод смены цвета, цвет меняется в соответствии с правилами структуры, родитель становится красный, дети черные
     * @param node - принимает текущую ноду
     */
    private void colorSwap(Node node) {
        node.rightChild.color = Color.BLACK;
        node.leftChild.color = Color.BLACK;
        node.color = Color.RED;
    }

    /**
     * Класс ноды
     */
    private class Node {
        /** Поле значение */
        private int value;
        /** Поле цвет */
        private Color color;
        /** Поле левый ребенок */
        private Node leftChild;
        /** Поле правый ребенок */
        private Node rightChild;

        /**
         * Перегруженный метод для корректного вывода
         */
        @Override
        public String toString() {
            return "Node{" + "value=" + value + ", color=" + color + "}";
        }
    }

    /** Возможные цвета для элементов красно-черного дерева */
    private enum Color {
        RED, BLACK
    }
}