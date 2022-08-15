/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   ThreadsCounter.java                                :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: cgretche <cgretche@student.21-school.ru    +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2022/07/22 20:02:41 by cgretche          #+#    #+#             */
/*   Updated: 2022/07/22 23:07:45 by cgretche         ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.util.List;

public class ThreadsCounter extends Thread {
	
	private static int readySum;

	int sumNumber, startIndex, endIndex;

	private static synchronized void addToSum(int sumNumber, int startIndex, int endIndex)	{
		System.out.println(Thread.currentThread().getName() +
		": from " + startIndex + " to " + endIndex + " sum is " + sumNumber);
		readySum += sumNumber;
	}

	public ThreadsCounter(List<Integer> listNum, int startIndex, int endIndex)	{
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		int tmp = 0;
		for (int i: listNum)	{
			tmp += i;
		}
		sumNumber = tmp;
	}

	public static int getReadySum()	{
		return (readySum);
	}

	@Override
	public void run()	{
		addToSum(sumNumber, startIndex, endIndex);
	}
}
