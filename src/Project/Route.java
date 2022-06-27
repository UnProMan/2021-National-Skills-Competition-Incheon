package Project;

import java.util.ArrayList;
import java.util.Collections;

import Base.Base;

public class Route implements Base{
	
	public static Route rt;
	
	int maps[][] = new int[70][70];
	int distance[] = new int[70];
	boolean check[] = new boolean[70];
	int arrive[] = new int[71];
	
	ArrayList<Integer> data= new ArrayList<>();
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	int start, fin;
	
	public void input(int x, int y, int w) {
		maps[x][y] = w;
		maps[y][x] = w;
	}
	
	public void route(int v) {
		
		Query("select * from point p1, point p2, connect c where p1.po_No = c.c_Node1 and p2.po_No = c.c_Node2;", list);
		
		for (int i = 0; i < list.size(); i++) {
			int cost = (int) Math.sqrt( Math.pow(intnum(list.get(i).get(1)) - intnum(list.get(i).get(4)), 2) + Math.pow(intnum(list.get(i).get(2)) - intnum(list.get(i).get(5)), 2) );
			input(intnum(list.get(i).get(0)) - 1, intnum(list.get(i).get(3)) - 1, cost);
		}
		
		for (int i = 0; i < 70; i++) {
			distance[i] = Integer.MAX_VALUE;
			arrive[i] = 0;
			check[i] = false;
		}
		
		distance[v] = 0;
		arrive[v] = 1000;
		check[v] = true;
		
		for (int i = 0; i < 70; i++) {
			if (!check[i] && maps[v][i] != 0) {
				distance[i] = maps[v][i];
			}
		}
		
		int min;
		int min_index;
		
		for (int i = 0; i < 69; i++) {
			
			min = Integer.MAX_VALUE;
			min_index = -1;
			
			for (int j = 0; j < 70; j++) {
				if (!check[j] && distance[j] != Integer.MAX_VALUE) {
					if (distance[j]< min) {
						min = distance[j];
						min_index  = j;
					}
				}
			}
			
			check[min_index] = true;
			for (int j = 0; j < 70; j++) {
				if (!check[j] && maps[min_index][j] != 0) {
					if (distance[j] > distance[min_index] + maps[min_index][j]) {
						distance[j] = distance[min_index] + maps[min_index][j];
						arrive[j] = min_index;
					}
				}
			}
			
		}
		
		Query("select * from connect where c_Node1 = ?", list, (start + 1) + "");
		for (int i = 0; i < list.size(); i++) {
			arrive[intnum(list.get(i).get(1)) - 1] = 1000;
		}
		
		Query("select * from connect where c_Node2 = ?", list, (start + 1) + "");
		for (int i = 0; i < list.size(); i++) {
			arrive[intnum(list.get(i).get(0)) - 1] = 1000;
		}
		
		data.add(fin);
		int de = arrive[fin];
		
		while (de != 1000) {
			data.add(de);
			de = arrive[de];
		}
		
		if (data.get(data.size() - 1) != start) {
			data.add(start);
		}
		
		Collections.reverse(data);
		
	}
	
	public void start(int start, int fin) {
		
		data = new ArrayList<>();
		this.start = start;
		this.fin = fin;
		route(start);
		
	}
	
	public Route() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void design() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
