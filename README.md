# HeartsData
A program for collecting data from Hearts games and outputting game/player statistics.

The program runs as a text-based interface which runs in a loop. The user enters an option, which will cause that particular iteration of the loop to do something different, be it changing a program setting or outputting data. Every user option has a corresponding method in the Query.java file: this is because the list of user options is defined by what public methods exist in Query.java. This means that adding a public method to Query.java will add a user option to the program! This makes adding features to HeartsData very simple, as I will explain later.

User-option methods can be added by any repository collaborator, but any method added should include the author of the method and the date it was added (in addition to a method description, although this is not required).

Although the program will accept case-insensitive user input (you can call option 'do the thing' by typing 'dO tHe ThInG'), user-options are formally considered to be all lower case. The naming convention for the corresponding method name is that the first letter of every word other than the first is capitalized, and then spaces are removed. For instance, option 'do the thing' would correspond to method doTheThing().

When the user calls an option, they can either enter the option name itself (eg. 'do the thing') to call that option, or they can enter the option name immediately followed by a question mark (eg. 'do the thing?') to simply have a description of that option printed. Either way, the corresponding method will be called, but the presence of a question mark will cause the single boolean argument to be true rather than false (explained below).

Every user-option method must be public and accept exactly one boolean parameter (denoted 'helpMode' below). If the method is called with 'helpMode' equal to true, then the program merely outputs a String describing the method and returns. If 'helpMode' is equal to false, then the actual method execution will occur (in the below example, <CODE HERE> will execute), allowing user input and data output to occur.

The format for a Query.java user-option method is specified below:
      /*
       * @author John Doe
       * @date 1/1/1970
       */
      public void doTheThing(boolean helpMode) {
      	String help = "<LINE 1>/n"
      				+ "<LINE 2>/n"
      				+ ...
      				+ "<LINE LAST>";
      	if (helpMode) {
      		System.out.println(help);
      		return;
      	}
      	
      	<CODE HERE>
      }
