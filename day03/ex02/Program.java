/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: cgretche <cgretche@student.21-school.ru    +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2022/07/22 19:45:08 by cgretche          #+#    #+#             */
/*   Updated: 2022/07/22 23:15:05 by cgretche         ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Program{

	private static int sumList(List<Integer> list) {
		int tmp = 0;
		
		for (int x: list) {
			tmp += x;
		}
		return tmp;
	}

	public static void main(String args[]) throws InterruptedException	{
		if (args.length != 2 || !args[0].startsWith("--arraySize")
			|| !args[1].startsWith("--threadsCount="))	{
			System.out.println("Wrong Args!!!");
			return ;
		}
		int arraySize = Integer.parseInt(args[0].replaceFirst("--arraySize=", ""));
		int threadsCount = Integer.parseInt(args[1].replaceFirst("--threadsCount=", ""));
		if (arraySize > 2000000 || arraySize < threadsCount)	{
			System.err.println("Wrong size of args");
			return ;
		}
		List<Integer> lists = new ArrayList<>(arraySize);

		
		for(int i = 0; i < arraySize; i++)	{
			int rand = ThreadLocalRandom.current().nextInt() % 1000;
			if (rand < 0)	{
				rand *= -1;
			}
			lists.add(rand);
		}
		System.out.println("Sum " + sumList(lists));
		Thread.sleep(1000);
		
		int range = arraySize / threadsCount;
		List<Thread> listOfThread = new ArrayList<>(threadsCount);

		int beginIndex = 0;
		int lastIndex = 0;

		for (int i = 0; i < threadsCount - 1; i++) {
			lastIndex += range;
			listOfThread.add(new ThreadsCounter(lists.subList(beginIndex, lastIndex + 1), beginIndex, lastIndex));
			beginIndex = lastIndex + 1;
		}
		beginIndex = lastIndex + 1;
		lastIndex = arraySize;

		listOfThread.add(new ThreadsCounter(lists.subList(beginIndex, lastIndex), beginIndex, lastIndex));
		for (Thread i: listOfThread)	{
			i.start();
		}

		for (Thread i: listOfThread)	{
			try {
				i.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Sum by threads: " + ThreadsCounter.getReadySum());
	}
}