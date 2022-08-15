/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: cgretche <cgretche@student.21-school.ru    +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2022/07/22 23:07:15 by cgretche          #+#    #+#             */
/*   Updated: 2022/07/22 23:07:16 by cgretche         ###   ########.fr       */
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
			Program.sayEgg();
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
			Program.sayHen();
		}
	}
}

class Program {
	public static boolean isEggPrinted = false;
	
	public static synchronized void sayHen() {
		if (isEggPrinted == false) {
			try {
				Program.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Hen");
		
		isEggPrinted = false;
		
		Program.class.notify();
	}

	public static synchronized void sayEgg() {
		if (isEggPrinted == true) {
			try {
				Program.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Egg");

		isEggPrinted = true;

		Program.class.notify();
	}
	
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
	}
}