/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: cgretche <cgretche@student.21-school.ru    +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2022/07/21 10:18:48 by cgretche          #+#    #+#             */
/*   Updated: 2022/07/22 23:07:01 by cgretche         ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

class Egg extends Thread	{
	private final int count;
	public Egg(int count)	{
		this.count = count;
	}

	@Override
	public void run(){
		for (int i = 0; i < count; i++)	{
			System.out.println("EGG");
		}
	}
}

class Hen extends Thread	{
	private final int count;
	public Hen(int count)	{
		this.count = count;
	}

	@Override
	public void run(){
		for (int i = 0; i < count; i++)	{
			System.out.println("HEN");
		}
	}
}

class Program {
	
	public static void main(String[] args) {
		
		if (args.length != 1 || !args[0].startsWith("--count=")) {
			System.out.println("Wrong number of arguments");
			System.exit(-1);
		}

		int count = Integer.parseInt(args[0].substring(8));
		
		Thread egg = new Egg(count);
		Thread hen = new Hen(count);

		egg.start();
		hen.start();

		try {
			hen.join();
			egg.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < count; i++) {
			System.out.println("HUMAN");
		}
	}
}