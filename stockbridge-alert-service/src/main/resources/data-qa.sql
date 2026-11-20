INSERT INTO alert (message,severity,status,created_at) SELECT 'QA environment ready','INFO','OPEN',CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM alert WHERE message='QA environment ready');
