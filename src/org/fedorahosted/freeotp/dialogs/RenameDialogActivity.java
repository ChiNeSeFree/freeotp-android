package org.fedorahosted.freeotp.dialogs;

import org.fedorahosted.freeotp.R;
import org.fedorahosted.freeotp.Token;
import org.fedorahosted.freeotp.TokenPersistence;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RenameDialogActivity extends BaseDialogActivity {
    public static final String FRAGMENT_TAG = "fragment_rename";
    public static final String POSITION     = "position";

    private TokenPersistence   mTokenPersistence;
    private EditText           mIssuer;
    private EditText           mLabel;
    private int                mPosition;

    public RenameDialogActivity() {
        super(R.layout.rename, android.R.string.cancel, R.string.restore_defaults, R.string.rename);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the position we wish to rename. This MUST exist.
        mPosition = getIntent().getIntExtra(POSITION, -1);
        if (mPosition < 0)
            finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mTokenPersistence = new TokenPersistence(this);
        Token token = mTokenPersistence.get(mPosition);

        mIssuer = (EditText) findViewById(R.id.issuer);
        mIssuer.setText(token.getIssuer());
        mIssuer.setSelection(mIssuer.getText().length());

        mLabel = (EditText) findViewById(R.id.label);
        mLabel.setText(token.getLabel());
    }

    @Override
    protected void onClick(View view, int which) {
        String issuer = "", label = "";

        switch (which) {
        case BUTTON_POSITIVE:
            issuer = mIssuer.getText().toString();
            label = mLabel.getText().toString();
        case BUTTON_NEUTRAL:
            if (issuer.length() == 0 && label.length() == 0) {
                issuer = null;
                label = null;
            }

            Token token = mTokenPersistence.get(mPosition);
            token.setIssuer(issuer);
            token.setLabel(label);
            mTokenPersistence.save(token);
        }
    }
}
