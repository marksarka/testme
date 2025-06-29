private String generateAggregationQuery(String portfolioId, String customerName, LocalDate fromDate, LocalDate toDate) {
    return """
        SELECT %s
            p.chain ->> '$[1]'                     AS portfolio_id,
            f.augmented_risk_tier                 AS violation_type,
            p.brand                               AS risk_tier,
            r.resolve_state                       AS brand,
            IF(rp.removed_date IS NULL, FALSE, TRUE) AS resolve_state,
            COUNT(*)                              AS remove_state
        FROM finding f
            INNER JOIN product p ON p.id = f.product_id
            LEFT JOIN resolution r ON f.id = r.finding_id
            LEFT JOIN removed_products rp ON rp.product_id_mp = p.product_id_mp
                AND rp.customer_name = f.customer
        WHERE %s is not null AND f.created BETWEEN '%s' AND DATE_ADD('%s', INTERVAL 1 DAY)
            AND f.customer = '%s'
        GROUP BY portfolio_id, violation_type, risk_tier, brand, resolve_state, remove_state;
        """.formatted(portfolioId, portfolioId, fromDate, toDate, customerName);
}
