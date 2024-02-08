package com.example.studenthub;

public class CustomArrayList<E> {
    private static final int DEFAULT_CAPACITY = 100000;
    private Object[] elements;
    private int size;

    public CustomArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void add(E element) {
        ensureCapacity(size + 1);
        elements[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (E) elements[index];
    }

    public int size() {
        return size;
    }

    public void clear() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E oldValue = get(index);
        elements[index] = element;
        return oldValue;
    }


    public void sort() {
        int n = size;

        // Bubble sort algorithm
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Extracting the first word from each name
                String name1 = ((Student) elements[j]).getName();
                String name2 = ((Student) elements[j + 1]).getName();
                String[] name1Words = name1.split(" ");
                String[] name2Words = name2.split(" ");

                // Comparing the first characters of the first words
                char firstCharName1 = name1Words[0].charAt(0);
                char firstCharName2 = name2Words[0].charAt(0);

                // If the first characters are in wrong order, swap elements
                if (firstCharName1 > firstCharName2) {
                    // Swap elements
                    E temp = (E) elements[j];
                    elements[j] = elements[j + 1];
                    elements[j + 1] = temp;
                }
            }
        }
    }


    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E removedElement = get(index);
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[size - 1] = null; // Set the last element to null
        size--;
        return removedElement;
    }

    public int indexOf(E element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(elements[i])) {
                return i;
            }
        }
        return -1; // Element not found
    }



    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = Math.max(elements.length * 2, minCapacity);
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public void addAll(CustomArrayList<E> collection) {
        ensureCapacity(size + collection.size);
        System.arraycopy(collection.elements, 0, elements, size, collection.size);
        size += collection.size;
    }
}
