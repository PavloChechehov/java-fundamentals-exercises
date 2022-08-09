package com.bobocode.fp;

import com.bobocode.fp.exception.EntityNotFoundException;
import com.bobocode.model.Account;
import com.bobocode.model.Sex;
import com.bobocode.util.ExerciseNotCompletedException;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.Month;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

/**
 * {@link CrazyStreams} is an exercise class. Each method represent some operation with a collection of accounts that
 * should be implemented using Stream API. Every method that is not implemented yet throws
 * {@link ExerciseNotCompletedException}.
 * <p>
 * TODO: remove exception throwing and implement each method using Stream API
 * <p><p>
 * <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com/learn">visit our website</a></strong>
 * <p>
 *
 * @author Taras Boychuk
 */
@AllArgsConstructor
public class CrazyStreams {
    private Collection<Account> accounts;

    /**
     * Returns {@link Optional} that contains an {@link Account} with the max value of balance
     *
     * @return account with max balance wrapped with optional
     */
    public Optional<Account> findRichestPerson() {
        return accounts.stream().max((o1, o2) -> o1.getBalance().compareTo(o2.getBalance()));
    }

    /**
     * Returns a {@link List} of {@link Account} that have a birthday month equal to provided.
     *
     * @param birthdayMonth a month of birth
     * @return a list of accounts
     */
    public List<Account> findAccountsByBirthdayMonth(Month birthdayMonth) {
        return accounts.stream()
                .filter(account -> account.getBirthday().getMonth().equals(birthdayMonth))
                .collect(Collectors.toList());
    }

    /**
     * Returns a map that separates all accounts into two lists - male and female. Map has two keys {@code true} indicates
     * male list, and {@code false} indicates female list.
     *
     * @return a map where key is true or false, and value is list of male, and female accounts
     */
    public Map<Boolean, List<Account>> partitionMaleAccounts() {
        return accounts.stream()
                .collect(groupingBy(account -> account.getSex() == Sex.MALE, toList()));
    }

    /**
     * Returns a {@link Map} that stores accounts grouped by its email domain. A map key is {@link String} which is an
     * email domain like "gmail.com". And the value is a {@link List} of {@link Account} objects with a specific email domain.
     *
     * @return a map where key is an email domain and value is a list of all account with such email
     */
    public Map<String, List<Account>> groupAccountsByEmailDomain() {
        return accounts.stream()
                .collect(groupingBy(account ->
                                account.getEmail().substring(account.getEmail().indexOf("@") + 1),
                        toList()));
    }

    /**
     * Returns a number of letters in all first and last names.
     *
     * @return total number of letters of first and last names of all accounts
     */
    public int getNumOfLettersInFirstAndLastNames() {
        return accounts
                .stream()
                .mapToInt(account -> account.getFirstName().length() + account.getLastName().length())
                .sum();
    }

    /**
     * Returns a total balance of all accounts.
     *
     * @return total balance of all accounts
     */
    public BigDecimal calculateTotalBalance() {
        BigDecimal result = BigDecimal.ZERO; // identity -> initial value

        BinaryOperator<BigDecimal> add = BigDecimal::add; // accumulator -> binaryOperator -> add method

        for (Account element : accounts) {
            result = add.apply(result, element.getBalance());
        }


//        List<Integer> integers = List.of(1, 2, 3, 4, 5);
//        Integer sum = integers.stream().reduce(0, Integer::sum);
//        Integer max = integers.stream().reduce(0, Integer::max);
//        Integer multiply = integers.stream().reduce(1, (integer, integer2) -> integer * integer2);
//        Integer reduce = integers.stream().reduce(0, (integer, integer2) -> integer - integer2);
//        Integer min = integers.stream().reduce(Integer::min).get();
//        Integer max2 = integers.stream().reduce(Integer::max).get();
//
//        System.out.println("sum = " + sum);
//        System.out.println("max = " + max);
//        System.out.println("multiply = " + multiply);
//        System.out.println("reduce = " + reduce);
//        System.out.println("min = " + min);
//        System.out.println("max2 = " + max2);

        List<Integer> ages = Arrays.asList(25, 30, 45, 28, 32);
        int computedAges = ages.parallelStream().reduce(0, (a, b) -> a + b, Integer::sum);
        System.out.println("computedAges = " + computedAges);

        return result;


//        return accounts
//                .stream()
//                .map(Account::getBalance)
////                .reduce((bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2))
////                .orElseThrow();
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Returns a {@link List} of {@link Account} objects sorted by first and last names.
     *
     * @return list of accounts sorted by first and last names
     */
    public List<Account> sortByFirstAndLastNames() {
        return accounts
                .stream()
                .sorted((o1, o2) -> {
                    int firstCompare = o1.getFirstName().compareTo(o2.getFirstName());
                    if (firstCompare != 0) {
                        return firstCompare;
                    } else {
                        return o1.getLastName().compareTo(o2.getLastName());
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Checks if there is at least one account with provided email domain.
     *
     * @param emailDomain
     * @return true if there is an account that has an email with provided domain
     */
    public boolean containsAccountWithEmailDomain(String emailDomain) {
        return accounts
                .stream()
                .map(account -> {
                    return account.getEmail().substring(account.getEmail().indexOf("@") + 1);
                })
                .anyMatch(email -> email.equals(emailDomain));
    }

    /**
     * Returns account balance by its email. Throws {@link EntityNotFoundException} with message
     * "Cannot find Account by email={email}" if account is not found.
     *
     * @param email account email
     * @return account balance
     */
    public BigDecimal getBalanceByEmail(String email) {
        return accounts
                .stream()
                .filter(account -> account.getEmail().equals(email))
                .map(Account::getBalance)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find Account by email=%s", email)));
    }

    /**
     * Collects all existing accounts into a {@link Map} where a key is account id, and the value is {@link Account} instance
     *
     * @return map of accounts by its ids
     */
    public Map<Long, Account> collectAccountsById() {
        return accounts
                .stream()
                .collect(toMap(Account::getId, identity()));
    }

    /**
     * Filters accounts by the year when an account was created. Collects account balances by its emails into a {@link Map}.
     * The key is {@link Account#email} and the value is {@link Account#balance}
     *
     * @param year the year of account creation
     * @return map of account by its ids the were created in a particular year
     */
    public Map<String, BigDecimal> collectBalancesByEmailForAccountsCreatedOn(int year) {
        return accounts
                .stream()
//                .sorted((o1, o2) -> o1.getCreationDate().compareTo(o2.getCreationDate()))
                .filter(account -> account.getCreationDate().getYear() == year)
                .collect(toMap(Account::getEmail, Account::getBalance));
    }

    /**
     * Returns a {@link Map} where key is {@link Account#lastName} and values is a {@link Set} that contains first names
     * of all accounts with a specific last name.
     *
     * @return a map where key is a last name and value is a set of first names
     */
    public Map<String, Set<String>> groupFirstNamesByLastNames() {
        return accounts
                .stream()
                .collect(groupingBy(Account::getLastName, mapping(Account::getFirstName, toSet())));
    }

    /**
     * Returns a {@link Map} where key is a birthday month, and value is a {@link String} that stores comma and space
     * -separated first names (e.g. "Polly, Dylan, Clark"), of all accounts that have the same birthday month.
     *
     * @return a map where a key is a birthday month and value is comma-separated first names
     */
    //todo: bomba raketa
    public Map<Month, String> groupCommaSeparatedFirstNamesByBirthdayMonth() {
        return accounts
                .stream()
                .collect(groupingBy(account -> account.getBirthday().getMonth(),
                        mapping(Account::getFirstName, joining(", "))));
    }

    /**
     * Returns a {@link Map} where key is a {@link Month} of {@link Account#creationDate}, and value is total balance
     * of all accounts that have the same value creation month.
     *
     * @return a map where key is a creation month and value is total balance of all accounts created in that month
     */
    public Map<Month, BigDecimal> groupTotalBalanceByCreationMonth() {
        return accounts
                .stream()
                .collect(groupingBy(account -> account.getCreationDate().getMonth(),
                        mapping(Account::getBalance,
                                reducing(BigDecimal.ZERO, BigDecimal::add))));
    }

    /**
     * Returns a {@link Map} where key is a letter {@link Character}, and value is a number of its occurrences in
     * {@link Account#firstName}.
     *
     * @return a map where key is a letter and value is its count in all first names
     */

    //todo: to hard for me!

    //Wow
    public Map<Character, Long> getCharacterFrequencyInFirstNames() {
        return accounts.stream()
                .map(Account::getFirstName)
                .flatMapToInt(String::chars)
                .mapToObj(c -> (char) c)
                .collect(groupingBy(identity(), counting()));



/*        return accounts
                .stream()
                .map(Account::getFirstName)
                .map(String::chars)                                      //convert from String value to IntStream of chars
                .flatMap(intStream -> intStream.mapToObj(c -> (char) c)) //convert IntStream to Stream<Character>
                .collect(groupingBy(identity(), counting()));            //<Specific char, sum of appear times - counting>
*/
        /*char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        HashMap<Character, Long> map = new HashMap<>();

        for (char c : alphabet) {
            long sum = 0;
            for (Account account : accounts) {
                String firstName = account.getFirstName();
                char[] chars = firstName.toCharArray();
                for (char aChar : chars) {
                    if (aChar == c) {
                        sum = sum + 1;
                    }
                }
            }

            map.put(c, sum);
        }

        return map;*/
    }

    /**
     * Returns a {@link Map} where key is a letter {@link Character}, and value is a number of its occurrences ignoring
     * case, in all {@link Account#firstName} and {@link Account#lastName} that are equal or longer than {@code  nameLengthBound}.
     * Inside the map, all letters should be stored in lower case.
     *
     * @return a map where key is a letter and value is its count ignoring case in all first and last names
     */
    //todo: can it finished only with hits. View the completed branch
    public Map<Character, Long> getCharacterFrequencyIgnoreCaseInFirstAndLastNames(int nameLengthBound) {
        return accounts.stream()
                .flatMap(a -> Stream.of(a.getFirstName(), a.getLastName()))
                .filter(name -> name.length() >= nameLengthBound)
                .map(String::toLowerCase)
                .flatMapToInt(String::chars)
                .mapToObj(c -> (char) c)
                .collect(groupingBy(identity(), counting()));

   /*     HashMap<Character, Long> map = new HashMap<>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        for (char c : alphabet) {
            long sum = 0;

            for (Account account : accounts) {
                String s = account.getFirstName().toLowerCase();

                if (s.length() >= nameLengthBound) {

                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) == c) {
                            sum++;
                        }
                    }
                }

                s = account.getLastName().toLowerCase();

                if (s.length() >= nameLengthBound) {

                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) == c) {
                            sum++;
                        }
                    }
                }
            }

            if (sum != 0) {
                map.put(c, sum);
            }
        }

        return map;*/
    }

}

