# TPCCCM

Shared library consumed by TPCcli (and potentially other TPC apps). Contains entity models with embedded SQL, domain controllers (FIFO lot matching, stock/option processing), DB connection management, and preferences.

## Database
- **Production only** — all SQL targets `hlhtxc5_dmOfx` (dataMart) and `hlhtxc5_dbOfx` (OFX source). No `_dev` databases are used. Do not reintroduce `_dev` references.
- DB host: `zeus:3306`, driver: MariaDB, credentials in `CMDBModel`
- Connection pool managed by `CMDBController`

## Package structure
| Package | Contents |
|---|---|
| `com.hpi.entities` | SQL constants + model POJOs for every dataMart table |
| `com.hpi.TPCCMcontrollers` | Business logic — FIFO matching (`StockController`, `OptionController`), DB ops (`CMDBController`) |
| `com.hpi.TPCCMprefs` | App config, DB model (`CMDBModel`), globals |
| `com.hpi.TPCCMsql` | Low-level SQL utilities |
| `com.hpi.entities.eTrade` | eTrade-specific models (`EtradeEquityHistoryModel`) |

## Transaction source architecture (branch: etrade-transactions, merged 2026-06-17)
- `OpeningStock`/`ClosingStock`/`OpeningOptions`/`ClosingOptions` are populated by `etradeHarness --transactions`, not by TPCcli or TPCCCM.
- The DBOFX_* SQL constants in `OpeningStockModel`, `ClosingStockModel`, `OpeningOptionModel`, `ClosingOptionModel` are **commented out** — they were the OFX-era population path and are no longer called from the dataMart flow.
- The ETRADE_* SQL constants that were drafted in these models are also **commented out** — superseded by etradeHarness populating the tables directly.
- Active SQL constants used by FIFO matching: `GET_OPENING_STOCK_BY_ACCT`, `GET_CLOSING_STOCK_BY_ACCT`, `GET_ALL_AVAIL` (options).

## Key entity models and their active SQL constants
| Model | Active SQL constants |
|---|---|
| `OpeningStockModel` | `GET_OPENING_STOCK_BY_ACCT` |
| `ClosingStockModel` | `GET_CLOSING_STOCK_BY_ACCT` |
| `OpeningOptionModel` | `GET_ALL_AVAIL`, `DMOFX_UPDATE_PRICES` |
| `ClosingOptionModel` | `GET_ALL_AVAIL`, `DMOFX_UPDATE_PRICES`, `UPDATE_UNITS` |
| `FIFOOpenTransactionModel` | FIFO open positions aggregation |
| `FIFOClosedTransactionModel` | FIFO closed positions aggregation |
| `ClientEquityAttributesModel` | Per-user ticker/sector attributes |

## Notes
- Entity model SQL uses `String.format(SQL, userId)` — `%s` is always `joomla_id` / `userId`
- `INSERT IGNORE` is the deduplication strategy throughout; PKs enforce uniqueness
- `StockController` and `OptionController` do in-memory FIFO lot matching, then write results to `OpenStockFIFO`/`OpenOptionFIFO` and closed equivalents
- `Sector` is sourced from `hlhtxc5_dmOfx.EquityInfo` (FinViz), not from OFX or eTrade tables
