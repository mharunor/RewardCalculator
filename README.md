# Customer Rewards API

Calculates reward points per customer, per month, and in total, from a record
of purchase transactions.

## Rules

- 2 points for every dollar spent **over $100** in a transaction
- 1 point for every dollar spent **between $50 and $100** in a transaction
- Example: a $120 purchase = 2×$20 + 1×$50 = **90 points**

## Run it

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080` and loads a sample 3-month data
set (Jan–Mar 2024, 4 customers) at startup — see
`SampleDataLoader` for the raw transactions.

## Run the tests

```bash
mvn test
```

## Endpoints

| Method | Path                              | Description                                   |
|--------|-----------------------------------|------------------------------------------------|
| GET    | `/api/rewards/customers`          | Monthly + total points for every customer       |
| GET    | `/api/rewards/customers/{id}`     | Monthly + total points for one customer (e.g. `C001`) |
| GET    | `/api/rewards/transactions`       | Raw transaction data set (for reference)        |

## Example

```bash
curl http://localhost:8080/api/rewards/customers/C001
```

```json
{
  "customerId": "C001",
  "customerName": "Alice Smith",
  "monthlyPoints": [
    { "month": "January 2024", "points": 130 },
    { "month": "February 2024", "points": 160 },
    { "month": "March 2024", "points": 250 }
  ],
  "totalPoints": 540
}
```

## Expected totals for the full sample data set

| Customer      | Jan 2024 | Feb 2024 | Mar 2024 | Total |
|---------------|---------:|---------:|---------:|------:|
| Alice Smith   |      130 |      160 |      250 |   540 |
| Bob Jones     |       99 |      350 |       60 |   509 |
| Carol White   |      850 |    (n/a) |       10 |   860 |
| David Lee     |    (n/a) |      110 |      220 |   330 |

Carol has no transactions in February and David has none in January —
those months are simply omitted from `monthlyPoints` (rather than shown
as zero), demonstrating that months with no purchases don't appear in
the breakdown.

## Design notes

- `RewardsCalculatorService` holds the pure point-calculation rule for a
  single transaction amount (kept separate from aggregation so it's easy
  to unit test in isolation, using `BigDecimal` to avoid floating-point
  rounding issues with currency).
- `RewardsService` groups transactions by customer and by calendar month
  (`yyyy-MM`) and sums points, using a `TreeMap` so months come back in
  chronological order.
- `TransactionRepository` is a simple in-memory store, standing in for
  a database; the service layer doesn't need to change if it's later
  swapped for a real Spring Data JPA repository.
- Data is seeded via a `CommandLineRunner` (`SampleDataLoader`) purely
  for demonstration — nothing needs to be POSTed manually.
