INSERT INTO fx_rate (currency, rate_to_usd)
VALUES
    ('USD', 1.00000000),
    ('INR', 0.01200000),
    ('GBP', 1.27000000),
    ('EUR', 1.08000000)
ON CONFLICT (currency) DO NOTHING;
