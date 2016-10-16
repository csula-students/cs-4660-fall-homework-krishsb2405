package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Node;


	class QNode{
		QNode next;
		Node data;
		int priority;
		public QNode(Node newData,int newPriority){
			data = newData;
			priority = newPriority;
			next = null;
		}
	}

	public class PriorityQueue{
		QNode head;
		
		public PriorityQueue(){
		head = null;
		}
		int getPriotiyOfNode(Node node){
			QNode currentNode = head;
			while(currentNode != null){
				if(currentNode.data.getData().equals(node.getData())){
					return currentNode.priority;
				}
				currentNode = currentNode.next;
			}
			return -1;
		}
		void remove(Node node){
			QNode currentNode = head;
			while(currentNode.next != null){
				if(currentNode.next.data.getData() == node.getData()){
					currentNode.next = currentNode.next.next;
					return;
				}
				currentNode = currentNode.next;
			}
			if(head.data.getData().equals(node.getData())){
				head = head.next;
			}
		}
		
		void enqueue(Node node, int priority){
			QNode newNode = new QNode(node, priority); 
			if(head == null){
				head = newNode;
			}
			else{
				QNode currentNode = head;
				while(currentNode.next != null && currentNode.next.priority < newNode.priority ){
					currentNode = currentNode.next;
				}
				if(head == currentNode){
					if(head.priority > newNode.priority){
						newNode.next = head;
						head = newNode;
					}
					else{
						newNode.next = head.next;
						head.next = newNode;
					}
				}else{
					if(currentNode.priority > newNode.priority){
						newNode.next = currentNode;
						currentNode = newNode;
					}
					else{
						newNode.next = currentNode.next;
						currentNode.next = newNode;
					}
				}
			}
		}
		Node dequeue(){
			if(head != null){
			Node data = head.data;
			head = head.next;
			return data;
			}
			
			return null;
		}
		boolean isEmpty(){
			return head == null;
		}
	}