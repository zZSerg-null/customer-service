DO $$
    BEGIN
        LOOP
            WITH duplicates AS (
                SELECT p.id, p.number, row_number() OVER (PARTITION BY p.number) AS row_number
                FROM passport p, passport t
                WHERE p.id <> t.id AND p.series = t.series AND p.number = t.number
                GROUP BY p.id
                ORDER BY p.number
            )
            UPDATE passport
            SET number = CAST(number AS INTEGER) + 1
            WHERE id IN (
                SELECT id
                FROM duplicates
                WHERE row_number = 1
            );
            IF NOT FOUND THEN
                EXIT;
            END IF;
        END LOOP;
    END
$$;